package com.xapps.persistence.attempt.answer

import com.xapps.model.attempt.record.ConfidenceCode

data class MsAnswerDocument(
    override val id: String,
    override val answerRecordId: String,
    val selectedOptionIds: Set<String>,
    override val confidenceCode: ConfidenceCode?
) : AnswerDocument