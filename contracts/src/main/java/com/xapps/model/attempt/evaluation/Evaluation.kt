package com.xapps.model.attempt.evaluation

import com.xapps.model.QuestionId
import kotlinx.serialization.Serializable

@Serializable
data class Evaluation(
    val id: String,
    val attemptId: String,
    val outcomes: List<QuestionOutcome>,
    val grade: Grade,
    val report: Report,
    val evaluatedAtMillis: Long
) {
    val correctCount: Int
        get() = outcomes.count { it.isCorrect }

    val incorrectCount: Int
        get() = outcomes.size - correctCount

    val percentage: Double
        get() = (correctCount.toDouble() / outcomes.size.toDouble()) * 100.0
}

@Serializable
data class QuestionOutcome(
    val id: String,
    val evaluationId: String,
    val questionId: QuestionId,
    val isCorrect: Boolean
)
