@file:OptIn(ExperimentalTime::class)

package com.xapps.time.clock

import com.xapps.time.types.KotlinInstant
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

interface ClockProvider {

    fun now(): KotlinInstant

    fun timeZone(): TimeZone
}
