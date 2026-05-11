package com.xapps.platform.core.outcome

import kotlinx.coroutines.CoroutineScope

/**
 * Internal interface for TrackedScope bookkeeping.
 *
 * Not visible to public users. Extensions can safely access delegate, jobs, and exceptions.
 */
internal sealed interface TrackedInternals {
    val delegate: CoroutineScope
    val jobs: AtomicJobSet
    val exceptions: AtomicThrowableSet
}

/** Internal helper to get internals from any TrackedScope */
internal fun TrackedScope.internals(): TrackedInternals =
    (this as? TrackedInternals).also { ensureOutcomeScopeElementPresent() }
        ?: error("TrackedScope is not a valid internal implementation")

/**
 * Checks whether an [OutcomeScopeElement] exists in the current coroutine context.
 * Throws an [IllegalStateException] if it is missing.
 *
 * Use this at the start of TrackedScope extension functions to enforce correct scoping.
 */
internal fun TrackedScope.ensureOutcomeScopeElementPresent() {
    val element = coroutineContext[OutcomeScopeElement]
    requireNotNull(element) {
        "OutcomeScopeElement is missing in the current coroutine context. " +
                "This TrackedScope extension function must be called within a valid outcome scope."
    }
}