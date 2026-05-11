package com.xapps.dto.job

import com.xapps.model.QuizId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun JobStatus.isNotFinal() = !isFinal()

@Serializable
sealed class JobStatus {

    @Serializable
    @SerialName("Queued")
    data object Queued : JobStatus()

    @Serializable
    @SerialName("Running")
    data class Running(
        val percentage: Int
    ) : JobStatus()

    @Serializable
    @SerialName("Completed")
    data class Completed(
        val quizId: QuizId
    ) : JobStatus()

    @Serializable
    @SerialName("Failed")
    data class Failed(
        val reason: String,
        val canRetry: Boolean
    ) : JobStatus()

    @Serializable
    @SerialName("Cancelled")
    data object Cancelled : JobStatus()
}

fun JobStatus.isFinal(): Boolean =
    this is JobStatus.Completed || (this is JobStatus.Failed && !canRetry) || this is JobStatus.Cancelled