package com.xapps.persistence.attempt.answer

import com.xapps.model.attempt.record.ConfidenceCode

sealed interface AnswerDocument {
    val id: String
    val answerRecordId: String
    val confidenceCode: ConfidenceCode?
}