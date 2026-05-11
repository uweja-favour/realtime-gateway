package com.xapps.persistence.attempt

import com.xapps.persistence.attempt.answer.AnswerDocument

data class AnswerRecordDocument(
    val id: String,
    val attemptId: String,
    val questionId: String,
    val answer: AnswerDocument
)