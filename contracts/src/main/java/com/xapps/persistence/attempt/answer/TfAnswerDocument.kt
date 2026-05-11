package com.xapps.persistence.attempt.answer

import com.xapps.model.attempt.record.ConfidenceCode

data class TfAnswerDocument(
    override val id: String,
    override val answerRecordId: String,
    val selectedOptionId: String,
    override val confidenceCode: ConfidenceCode?
) : AnswerDocument