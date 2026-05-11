package com.xapps.platform.core.outcome

import com.xapps.platform.core.outcome.domain_error.DomainError
import com.xapps.platform.core.outcome.domain_error.toDomainError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.plus
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext

/**
 * Represents the result of an operation: either [Success] with a value,
 * or [Failure] with a [DomainError].
 */
sealed class Outcome<out T> {

    data class Success<T>(val value: T) : Outcome<T>()
    data class Failure(val error: DomainError) : Outcome<Nothing>()

    companion object {
        fun <T> success(value: T): Outcome<T> = Success(value)
        fun failure(error: DomainError): Outcome<Nothing> = Failure(error)
        fun failure(t: Throwable): Outcome<Nothing> = failure(t.toDomainError())
    }

    /** Returns the exception if this is a failure, null otherwise. */
    fun exceptionOrNull(): Throwable? = when (this) {
        is Failure -> error.exception
        else -> null
    }

    override fun toString(): String = fold(
        onSuccess = {
            "Outcome.Success | value=$it"
        },
        onFailure = {
            "Outcome.Failure | error=$it"
        }
    )
}


/** Aggregates multiple [Throwable]s into one exception. */
public class AggregatedException(val causes: List<Throwable>) : RuntimeException(
    causes.firstOrNull()?.message ?: "Multiple exceptions (${causes.size})"
) {
    init { causes.forEach(::addSuppressed) }

    override fun toString(): String = "AggregatedException(${causes.size} causes): " +
            causes.joinToString { it.toString() }
}

/** Convert a Throwable into an Outcome.Failure */
public fun Throwable.asFailure(): Outcome<Nothing> = Outcome.failure(this)

/** Builds a single Throwable from a collection, ignoring [CancellationException] */
public fun buildCauseChain(causes: Iterable<Throwable>): Throwable? =
    buildCauseChainImpl(causes.toList())

internal fun buildCauseChain(causes: AtomicThrowableSet): Throwable? =
    buildCauseChainImpl(causes.snapshot().toList())

private fun buildCauseChainImpl(causes: Collection<Throwable>): Throwable? {
    if (causes.isEmpty()) return null

    val relevant = causes.filterNot { it is CancellationException }.distinct()
    return when (relevant.size) {
        0 -> null
        1 -> relevant.first()
        else -> AggregatedException(relevant)
    }
}

@Suppress("UNCHECKED_CAST")
fun <R> Outcome<*>.flattenIfNested(): Outcome<R> {
    tailrec fun flatten(outcome: Outcome<*>): Outcome<*> = when (outcome) {
        is Outcome.Success<*> -> {
            val value = outcome.value
            if (value is Outcome<*>) flatten(value) else outcome
        }
        else -> outcome
    }

    return flatten(this) as Outcome<R>
}

// ---------------------------

/**
 * Executes a suspend block in a [TrackedScope], collecting all exceptions from child coroutines.
 * - CancellationException propagates immediately.
 * - All other exceptions are aggregated into Outcome.Failure.
 */
public suspend fun <R> outcomeOf(block: suspend TrackedScope.() -> R): Outcome<R> =
    outcomeOfImpl(block)

/** Synchronous safe wrapper */
public inline fun <R> outcomeSync(block: () -> R): Outcome<R> = runCatching(block).asOutcome()

internal suspend fun <R> outcomeOfImpl(block: suspend TrackedScope.() -> R): Outcome<R> {
    val parent = currentCoroutineContext()[OutcomeScopeElement]
    val result = parent?.let { handleNestedExecution(it, block) } ?: handleRootExecution(block)
    return result.flattenIfNested()
}

// Context element used to mark an outcome suspend root scope
internal class OutcomeScopeElement(
    val exceptions: AtomicThrowableSet,
    val exceptionHandler: CoroutineExceptionHandler
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<OutcomeScopeElement>
    override val key: CoroutineContext.Key<*> get() = Key
    override fun toString(): String =
        "OutcomeScopeElement(exceptions_size=${exceptions.snapshot().size}) | " + super.toString()
}

private suspend fun <R> handleNestedExecution(
    parent: OutcomeScopeElement,
    block: suspend TrackedScope.() -> R
): Outcome<R> = supervisorScope {

    println("NESTED EXECUTION!")
    val jobs = AtomicJobSet()
    val exceptions = AtomicThrowableSet()
    val tracked = TrackedScope(this, jobs, exceptions)

    val result = runCatching { tracked.block() }
    jobs.joinAllCatching()

    println("The root exception is: ${result.exceptionOrNull()}")
    result.exceptionOrNull()?.also(exceptions::add)
    exceptions.snapshot().forEach(parent.exceptions::add)

    buildCauseChain(exceptions)?.asFailure() ?: result.asOutcome()
}

private suspend fun <R> handleRootExecution(
    block: suspend TrackedScope.() -> R
): Outcome<R> = supervisorScope {
    println("ROOT EXECUTION!")
    val jobs = AtomicJobSet()
    val exceptions = AtomicThrowableSet()

    val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable !is CancellationException) exceptions.add(throwable).also {
            println("FATAL: ⚠️ Handler caught: ${throwable.message}")
        }
    }

    val outcomeElement = OutcomeScopeElement(exceptions, handler)
    val context = coroutineContext + outcomeElement + handler
    // include the context here so launch { } called directly in here do not lose the outcomeElement
    val tracked = TrackedScope(this + context, jobs, exceptions)

    // withContext propagates the outcomeElement down to nested outcomeOf {}
    val result = runCatching { withContext(context) { tracked.block() } }
    jobs.joinAllCatching()

//    println("The root exception is: ${result.exceptionOrNull()}")
    result.exceptionOrNull()?.also(exceptions::add)
    buildCauseChain(exceptions)?.asFailure() ?: result.asOutcome()
}




// ---------------------------
// Common Outcome extensions
// ---------------------------

public fun <T> Outcome<T>.isSuccess(): Boolean = this is Outcome.Success
public fun <T> Outcome<T>.isFailure(): Boolean = this is Outcome.Failure
public fun <T> Outcome<T>.getOrNull(): T? = (this as? Outcome.Success)?.value
public fun <T> Outcome<T>.getOrThrow(): T = fold(
    onSuccess = { it },
    onFailure = { throw it }
)
public inline fun <T> Outcome<T>.getOrElse(block: (Throwable) -> T): T = fold(
    onSuccess = { it },
    onFailure = { block(it) }
)

public fun <T> Outcome<T>.getOrDefault(default: T): T = (this as? Outcome.Success)?.value ?: default
public fun <T> Outcome<T>.errorOrNull(): DomainError? = (this as? Outcome.Failure)?.error
public inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = fold(
    onSuccess = { Outcome.success(transform(it)) },
    onFailure = { it.asFailure() }
)

public inline fun <T> Outcome<T>.recover(recovery: (DomainError) -> T): Outcome<T> = fold(
    onSuccess = { Outcome.success(it) },
    onFailure = { Outcome.success(recovery(it.toDomainError())) }
)

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> Outcome<T>.fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return exceptionOrNull()?.let(onFailure) ?: onSuccess((this as Outcome.Success).value)
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> Outcome<T>.onFailure(action: (DomainError) -> Unit): Outcome<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    (this as? Outcome.Failure)?.error?.let(action)
    return this
}

@OptIn(ExperimentalContracts::class)
public inline fun <T> Outcome<T>.onSuccess(action: (T) -> Unit): Outcome<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    (this as? Outcome.Success)?.value?.let(action)
    return this
}

public fun <T> Result<T>.asOutcome(): Outcome<T> = fold(
    onSuccess = { Outcome.success(it) },
    onFailure = { it.asFailure() }
)

public fun <T> Outcome<T>.toResult(): Result<T> = fold(
    onSuccess = { Result.success(it) },
    onFailure = { Result.failure(it) }
)

@SinceKotlin("1.3")
public inline fun <R, T> Outcome<T>.mapCatching(crossinline transform: (value: T) -> R): Outcome<R> = fold(
    onSuccess = { outcomeSync { transform(it) } },
    onFailure = { it.asFailure() }
)

/**
 * Evaluates this [Outcome] and maps it into a return value of type [R].
 *
 * - If this is [Outcome.Success], calls [onSuccess] with the success value.
 * - If this is [Outcome.Failure], calls [onFailure] with the associated [DomainError].
 *
 * This is ideal for controller endpoints that need to return a [ResponseEntity] or any other type
 * based on whether the operation succeeded or failed.
 */
public inline fun <T, R> Outcome<T>.respond(
    onSuccess: (T) -> R,
    onFailure: (DomainError) -> R
): R = fold(
    onSuccess = { onSuccess(it) },
    onFailure = { onFailure(it.toDomainError()) }
)
