package com.xapps.model.attempt.record

import com.xapps.model.QuestionId
import kotlinx.serialization.Serializable

@Serializable
data class AnswerRecord(
    val id: String,
    val attemptId: String,
    val questionId: QuestionId,
    val answer: Answer
)