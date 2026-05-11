@file: OptIn(ExperimentalTime::class)

package com.xapps.time.clock

import com.xapps.time.types.KotlinInstant
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SystemClockProvider(
    private val zone: TimeZone = TimeZone.UTC
) : ClockProvider {

    override fun now(): KotlinInstant = Clock.System.now()

    override fun timeZone(): TimeZone = zone
}