package com.xapps.platform.core.atomic

import kotlinx.atomicfu.atomic

/**
 * A simple lock-free queue for KMP, safe for concurrent access in coroutines.
 * Not suitable for heavy multi-threaded Java-style concurrency, but fine for Kotlin coroutines.
 */
class AtomicQueue<T> {
    private val _list = atomic<List<T>>(emptyList())

    /** Add an element to the end of the queue */
    fun add(element: T) {
        while (true) {
            val current = _list.value
            val updated = current + element
            if (_list.compareAndSet(current, updated)) return
        }
    }

    /** Remove and return the first element, or null if empty */
    fun poll(): T? {
        while (true) {
            val current = _list.value
            if (current.isEmpty()) return null
            val element = current.first()
            val updated = current.drop(1)
            if (_list.compareAndSet(current, updated)) return element
        }
    }

    inline fun forEach(action: (T) -> Unit) {
        val curValue = this.snapshot()
        for (element in curValue) action(element)
    }

    fun toList(): List<T> = this.snapshot().toList()

    /** Peek at the first element without removing */
    fun peek(): T? = _list.value.firstOrNull()

    /** Return true if queue is empty */
    fun isEmpty(): Boolean = _list.value.isEmpty()

    /** Return true if queue is not empty */
    fun isNotEmpty(): Boolean = _list.value.isNotEmpty()


    /** Return current size */
    val size: Int get() = _list.value.size

    /** Clear all elements */
    fun clear() {
        _list.value = emptyList()
    }

    /** Return all elements as a snapshot */
    fun snapshot(): List<T> = _list.value
}