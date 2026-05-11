package com.xapps.platform.core

import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

suspend fun <T> retryWithExponentialBackoff(
    maxDuration: Duration = 1.minutes,
    initialDelay: Duration = 1.seconds,
    maxDelay: Duration = 16.seconds,
    block: suspend () -> T?
): T {
    val startTime = System.currentTimeMillis()
    var attemptDelay = initialDelay

    while (true) {
        val result = block()
        result?.let { return it }

        val elapsed = System.currentTimeMillis() - startTime
        if (elapsed >= maxDuration.inWholeMilliseconds) {
            throw IllegalStateException("Block did not return a value within $maxDuration")
        }

        delay(min(attemptDelay.inWholeMilliseconds, (maxDuration.inWholeMilliseconds - elapsed)))
        attemptDelay = if ((attemptDelay * 2) > maxDelay) maxDelay else attemptDelay
    }
}