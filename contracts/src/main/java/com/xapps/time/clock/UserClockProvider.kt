package com.xapps.time.clock

import com.xapps.time.types.KotlinInstant
import kotlinx.datetime.TimeZone
import kotlin.time.Clock

class UserClockProvider(
    private val timeZone: TimeZone
) : ClockProvider {

    override fun now(): KotlinInstant = Clock.System.now()

    override fun timeZone(): TimeZone = timeZone
}