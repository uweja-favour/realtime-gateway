package com.xapps.platform.core.atomic

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/**
 * A production-safe, multiplatform-ready wrapper around kotlinx.atomicfu.atomic.
 * Works on JVM, Android, iOS, JS, and Native.
 */
class AtomicState<T> (initialValue: T) {

    private val delegate = atomic(initialValue)

    /**
     * Get the current value.
     */
    fun get(): T = delegate.value

    /**
     * Set a new value atomically.
     */
    fun set(newValue: T) {
        while (true) {
            val prev = delegate.value
            if (delegate.compareAndSet(prev, newValue)) return
        }
    }

    /**
     * Atomically set a new value and return the old one.
     */
    fun getAndSet(newValue: T): T = delegate.getAndSet(newValue)

    /**
     * Compare current value with [expect]; if equal, replace with [update].
     * Returns true if the update was performed.
     */
    fun compareAndSet(expect: T, update: T): Boolean =
        delegate.compareAndSet(expect, update)

    /**
     * Atomically updates the value using the provided function.
     */
    fun update(transform: (T) -> T) {
        delegate.update(transform)
    }

    /**
     * Returns the previous value and applies the transform atomically.
     */
    fun getAndUpdate(transform: (T) -> T): T {
        while (true) {
            val prev = delegate.value
            val next = transform(prev)
            if (delegate.compareAndSet(prev, next)) return prev
        }
    }

    /**
     * Updates the current value atomically and returns the updated value.
     */
    fun updateAndGet(transform: (T) -> T): T {
        while (true) {
            val prev = delegate.value
            val next = transform(prev)
            if (delegate.compareAndSet(prev, next)) return next
        }
    }

    override fun toString(): String = "AtomicState(value=${delegate.value})"
}

