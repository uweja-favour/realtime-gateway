package com.xapps.persistence.attempt.answer

import com.xapps.model.attempt.record.ConfidenceCode

data class FibAnswerDocument(
    override val id: String,
    override val answerRecordId: String,
    val fibTextAnswer: String,
    override val confidenceCode: ConfidenceCode?
) : AnswerDocument