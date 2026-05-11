@file: OptIn(ExperimentalTime::class)

package com.xapps.model.attempt

import com.xapps.model.attempt.evaluation.Evaluation
import com.xapps.time.clock.ClockProvider
import com.xapps.time.clock.SystemClockProvider
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalSerializationApi::class)
@Serializable
sealed class AttemptState {

    //  activeDurationMs: Long    // total accumulated *active* time

    @Serializable
    @SerialName("NotStarted")
    data object NotStarted : AttemptState()

    @Serializable
    @SerialName("OnGoing")
    data class OnGoing(
        @Contextual val startedAt: KotlinInstant,
        val activeDurationMillis: Long,
        @Contextual val lastEventAt: KotlinInstant
    ) : AttemptState()

    @Serializable
    @SerialName("Paused")
    data class Paused(
        @Contextual val startedAt: KotlinInstant,
        val activeDurationMillis: Long,
    ) : AttemptState()

    @Serializable
    @SerialName("AwaitingEvaluation")
    data class AwaitingEvaluation(
        @Contextual val startedAt: KotlinInstant,
        @Contextual val submittedAt: KotlinInstant,
        val activeDurationMillis: Long
    ) : AttemptState()

    @Serializable
    @SerialName("Evaluated")
    data class Evaluated(
        @Contextual val startedAt: KotlinInstant,
        @Contextual val submittedAt: KotlinInstant,
        val activeDurationMillis: Long,
        val evaluation: Evaluation,
        @Contextual val evaluatedAt: KotlinInstant
    ) : AttemptState()
}

fun AttemptState.effectiveElapsed(clockProvider: ClockProvider = SystemClockProvider()): Long =
    when (this) {
        is AttemptState.OnGoing -> {
            activeDurationMillis + (
                    clockProvider.now().toEpochMilliseconds() - lastEventAt.toEpochMilliseconds()
                    )
        }

        is AttemptState.Paused -> activeDurationMillis
        is AttemptState.AwaitingEvaluation -> activeDurationMillis
        is AttemptState.Evaluated -> activeDurationMillis

        is AttemptState.NotStarted -> 0L
    }
