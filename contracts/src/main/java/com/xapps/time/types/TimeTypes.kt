@file:OptIn(ExperimentalTime::class)

package com.xapps.time.types

import com.xapps.time.clock.ClockProvider
import com.xapps.time.clock.SystemClockProvider
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

typealias KotlinInstant = Instant
typealias JavaInstant = java.time.Instant
typealias KotlinLocalDate = LocalDate
typealias KotlinLocalDateTime = LocalDateTime
typealias KotlinDuration = Duration


fun Long.toKotlinInstant(): KotlinInstant =
    KotlinInstant.fromEpochMilliseconds(this)


/**
 * Converts epoch milliseconds to LocalDate in the specified time zone
 */
fun Long.toLocalDate(clockProvider: ClockProvider = SystemClockProvider()): KotlinLocalDate {
    return KotlinInstant.fromEpochMilliseconds(this).toLocalDateTime(clockProvider.timeZone()).date
}


/**
 * Converts a LocalDateTime to epoch millis (Long) in UTC.
 */
fun KotlinLocalDateTime.toEpochMillis(clockProvider: ClockProvider = SystemClockProvider()): Long =
    toInstant(clockProvider.timeZone()).toEpochMilliseconds()


/**
 * Converts epoch millis (Long) to LocalDateTime in UTC.
 */
fun Long.toLocalDateTime(clockProvider: ClockProvider = SystemClockProvider()): KotlinLocalDateTime {
    return KotlinInstant.fromEpochMilliseconds(this).toLocalDateTime(clockProvider.timeZone())
}
