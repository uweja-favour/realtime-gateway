package com.xapps.persistence.attempt

import com.xapps.model.QuestionId

data class QuestionOutcomeDocument(
    val id: String,
    val evaluationId: String,
    val questionId: QuestionId,
    val isCorrect: Boolean
)