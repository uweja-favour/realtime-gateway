@file: OptIn(ExperimentalTime::class)

package com.xapps.platform.core.time

import com.xapps.time.clock.SystemClockProvider
import com.xapps.time.types.KotlinInstant
import com.xapps.time.types.KotlinLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

private val clockProvider = SystemClockProvider()

// ----------------- Now helpers -----------------
val nowInKotlinInstant: KotlinInstant
    get() = clockProvider.now()

fun nowInKotlinInstant(): KotlinInstant = clockProvider.now()

fun kotlinInstantOfEpochMillis(l: Long): KotlinInstant =
    KotlinInstant.fromEpochMilliseconds(l)

fun nowInKotlinLocalDateTime(timeZone: TimeZone = clockProvider.timeZone()): KotlinLocalDateTime {
    return clockProvider.now().toLocalDateTime(timeZone)
}