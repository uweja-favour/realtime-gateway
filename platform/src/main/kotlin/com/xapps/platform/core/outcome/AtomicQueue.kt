package com.xapps.platform.core.outcome

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.Job

/**
 * A simple lock-free queue for KMP, safe for concurrent access in coroutines.
 * Not suitable for heavy multi-threaded Java-style concurrency, but fine for Kotlin coroutines.
 */
public class AtomicSet<T> {
    private val ref = atomic<Set<T>>(emptySet())

    public fun add(element: T): Unit = ref.update { it + element }

    public fun snapshot(): Set<T> = ref.value
}



// Internal atomic set for jobs
internal class AtomicJobSet {
    private val _set = atomic<Set<Job>>(emptySet())

    fun add(item: Job): Boolean {
        while (true) {
            val current = _set.value
            if (item in current) return false
            val newSet = current + item
            if (_set.compareAndSet(current, newSet)) return true
        }
    }

    fun remove(item: Job): Boolean {
        while (true) {
            val current = _set.value
            if (item !in current) return false
            val newSet = current - item
            if (_set.compareAndSet(current, newSet)) return true
        }
    }

    fun snapshot(): Set<Job> = _set.value
    fun isEmpty(): Boolean = _set.value.isEmpty()
    fun clear() { _set.value = emptySet() }
}

internal class AtomicThrowableSet {
    private val _set = atomic<Set<Throwable>>(emptySet())

    fun add(t: Throwable): Boolean {
        while (true) {
            val current = _set.value
            if (t in current) return false
            val newSet = current + t
            if (_set.compareAndSet(current, newSet)) return true
        }
    }

    fun remove(t: Throwable): Boolean {
        while (true) {
            val current = _set.value
            if (t !in current) return false
            val newSet = current - t
            if (_set.compareAndSet(current, newSet)) return true
        }
    }

    fun snapshot(): Set<Throwable> = _set.value
    fun isEmpty(): Boolean = _set.value.isEmpty()
    fun clear() { _set.value = emptySet() }
}
