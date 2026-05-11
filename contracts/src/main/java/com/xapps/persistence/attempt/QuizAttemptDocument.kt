package com.xapps.persistence.attempt

import com.xapps.model.QuizId

data class QuizAttemptDocument(
    val id: String,
    val quizId: QuizId,
    val attemptNumber: Int,

    val answers: List<AnswerRecordDocument>,
    val state: AttemptStateDocument
)