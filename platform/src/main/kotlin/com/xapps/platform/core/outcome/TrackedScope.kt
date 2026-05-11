package com.xapps.platform.core.outcome

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

/**
 * A coroutine scope that automatically tracks its launched child coroutines and exceptions.
 *
 * You can use [launch], [async], [withContext], [withTimeout], etc. on this scope,
 * just like a regular [CoroutineScope].
 */
@DslMarker
annotation class TrackedScopeMarker

@TrackedScopeMarker
public sealed interface TrackedScope {
    public val coroutineContext: CoroutineContext
}

/** Factory function for internal TrackedScope creation */
internal fun TrackedScope(
    delegate: CoroutineScope,
    jobs: AtomicJobSet,
    exceptions: AtomicThrowableSet
): TrackedScope = TrackedScopeImpl(delegate, jobs, exceptions)
    .also { it.ensureOutcomeScopeElementPresent() }

/** Internal TrackedScope implementation — no coroutine builders here */
internal class TrackedScopeImpl internal constructor(
    override val delegate: CoroutineScope,
    override val jobs: AtomicJobSet,
    override val exceptions: AtomicThrowableSet
) : TrackedScope, TrackedInternals {
    override val coroutineContext: CoroutineContext get() = delegate.coroutineContext
}

private fun TrackedScopeImpl.asTrackedScope(): TrackedScope = this as TrackedScope

