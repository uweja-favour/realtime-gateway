package com.xapps.platform.core.outcome

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

/** Launch a tracked coroutine */
public fun TrackedScope.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend TrackedScope.() -> Unit
): Job {
    val internals = internals()
    val (delegate, jobs, exceptions) = Triple(internals.delegate, internals.jobs, internals.exceptions)

    // this coroutineContext right here is the immediate parent coroutineContext
    val childContext = coroutineContext + context

    val job = delegate.launch(childContext, start) {
        // We are now inside the body of the *real* coroutine launched by CoroutineScope.launch.
        // At this point, coroutineContext refers to THIS child coroutine's context,
        // which includes the newly created Job (because of the call above with childContext).

        // Create a new TrackedScope using this child's coroutineContext,
        // so nested `launch {}` calls inside the block will resolve to TrackedScope.launch.
        val current = TrackedScope(this, jobs, exceptions)

        // Execute the user-supplied suspend block with `current` as the receiver,
        // ensuring nested launches are also tracked.
        try {
            current.block()
        } catch (t: Throwable) {
            if (t !is CancellationException) exceptions.add(t)
            // cancel all parent; launch or async that do not have a supervisor job
            // in its context
            throw t
        }
    }

    // track the job and record exactly once on completion
    jobs.add(job)

    job.invokeOnCompletion { cause ->
        if (cause != null && cause !is CancellationException) {
            exceptions.add(cause)
        }
    }

    return job
}

/** Async variant */
public fun <T> TrackedScope.async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend TrackedScope.() -> T
): Deferred<T> {
    val childContext = coroutineContext + context

    val internals = internals()
    val (delegate, jobs, exceptions) = Triple(internals.delegate, internals.jobs, internals.exceptions)

    val deferred = delegate.async(childContext, start) {
        val current = TrackedScope(this, jobs, exceptions)
        try {
            current.block()
        } catch (t: Throwable) {
            if (t !is CancellationException) exceptions.add(t)
            throw t
        }
    }

    jobs.add(deferred)
    deferred.invokeOnCompletion { cause ->
        if (cause != null && cause !is CancellationException) exceptions.add(cause)
    }

    return deferred
}

/** withContext variant */
public suspend fun <T> TrackedScope.withContext(
    context: CoroutineContext,
    block: suspend TrackedScope.() -> T
): T = kotlinx.coroutines.withContext(coroutineContext + context) {

    val internals = internals()
    val (jobs, exceptions) = Pair(internals.jobs, internals.exceptions)

    val current = TrackedScope(this, jobs, exceptions)
    try {
        current.block()
    } catch (t: Throwable) {
        if (t !is CancellationException) exceptions.add(t)
        throw t
    }
}

/** Timeout helpers */
public suspend fun <T> TrackedScope.withTimeout(
    timeMillis: Long,
    block: suspend TrackedScope.() -> T
): T {
    val internals = internals()
    val (jobs, exceptions) = Pair(internals.jobs, internals.exceptions)

    return try {
        kotlinx.coroutines.withTimeout(timeMillis) {

            val current = TrackedScope(this, jobs, exceptions)
            current.block()
        }
    } catch (t: Throwable) {
        if (t !is CancellationException) exceptions.add(t)
        throw t
    }
}

public suspend fun <T> TrackedScope.withTimeout(
    timeout: Duration,
    block: suspend TrackedScope.() -> T
): T = withTimeout(timeout.inWholeMilliseconds, block)

public suspend fun <T> TrackedScope.withTimeoutOrNull(
    timeMillis: Long,
    block: suspend TrackedScope.() -> T
): T? {
    val internals = internals()
    val (jobs, exceptions) = internals.jobs to internals.exceptions

    return try {
        kotlinx.coroutines.withTimeoutOrNull(timeMillis) {
            val current = TrackedScope(this, jobs, exceptions)
            current.block()
        }
    } catch (t: Throwable) {
        if (t !is CancellationException) exceptions.add(t)
        throw t
    }
}

public suspend fun <T> TrackedScope.withTimeoutOrNull(
    timeout: Duration,
    block: suspend TrackedScope.() -> T
): T? = withTimeoutOrNull(timeout.inWholeMilliseconds, block)

@OptIn(InternalCoroutinesApi::class)
public fun <E> TrackedScope.produce(
    context: CoroutineContext = EmptyCoroutineContext,
    capacity: Int = 0,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onCompletion: CompletionHandler? = null,
    block: suspend TrackedScope.(ProducerScope<E>) -> Unit
): ReceiveChannel<E> {
    val internals = internals()
    val (delegate, jobs, exceptions) = Triple(internals.delegate, internals.jobs, internals.exceptions)
    val childContext = coroutineContext + context

    return delegate.produce(
        context = childContext,
        capacity = capacity,
        start = start,
        onCompletion = onCompletion
    ) {
        val current = TrackedScope(this, jobs, exceptions)
        try {
            current.block(this)
        } catch (t: Throwable) {
            if (t !is CancellationException) exceptions.add(t)
            throw t
        }

        coroutineContext[Job]?.invokeOnCompletion { cause ->
            if (cause != null && cause !is CancellationException) {
                exceptions.add(cause)
            }
        }
    }
}

/** Ensures current coroutine is active */
public fun TrackedScope.ensureActive() {
    val internals = internals()
    val exceptions = internals.exceptions

    try {
        coroutineContext.ensureActive()
    } catch (t: Throwable) {
        if (t !is CancellationException) exceptions.add(t)
        throw t
    }
}

/** Joins all tracked jobs, ignoring cancellations */
internal suspend fun AtomicJobSet.joinAllCatching() {
    for (job in snapshot()) {
        try {
            job.join()
        } catch (_: Throwable) {
            // ignore cancellations
        }
    }
}