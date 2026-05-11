@file:OptIn(ExperimentalTime::class)

package com.xapps.time.model

import com.xapps.time.types.KotlinInstant
import kotlin.time.ExperimentalTime

data class InstantRange(
    val start: KotlinInstant,
    val endExclusive: KotlinInstant
) {
    init {
        require(start <= endExclusive) {
            "start must be <= endExclusive"
        }
    }

    operator fun contains(instant: KotlinInstant): Boolean =
        instant in start..<endExclusive
}

infix fun KotlinInstant.until(endExclusive: KotlinInstant): InstantRange =
    InstantRange(this, endExclusive)
