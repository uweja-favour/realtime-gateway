package com.xapps.model.attempt

import com.xapps.model.attempt.record.AnswerRecord
import com.xapps.model.QuizId
import com.xapps.model.attempt.evaluation.Evaluation
import kotlinx.serialization.Serializable

@Serializable
data class QuizAttempt(
    val id: String,
    val parentId: String,
    val attemptNumber: Int,
    val answers: List<AnswerRecord>,
    val state: AttemptState
) {
    fun isUnfinished(): Boolean =
        state !is AttemptState.Evaluated

    fun score(): Double? {
        return (state as? AttemptState.Evaluated)
            ?.evaluation
            ?.percentage
    }

    fun QuizAttempt.evaluationOrNull(): Evaluation? =
        (state as? AttemptState.Evaluated)?.evaluation
}
