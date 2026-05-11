@file:OptIn(ExperimentalTime::class)

package com.xapps.model

import kotlin.time.ExperimentalTime

@JvmInline
value class QuestionTypeCode(val value: String)

// IMPORTANT: NEVER ALTER THE CODE
enum class QuestionType(val code: QuestionTypeCode, val displayName: String) {
    MC(QuestionTypeCode("MC"),"Multiple Choice"),
    MS(QuestionTypeCode("MS"),"Multiple Selection"),
    TF(QuestionTypeCode("TF"),"TrueOrFalse"),
    FIB(QuestionTypeCode("FIB"),"Fill-In-The-Blank");

    companion object {
        private val BY_CODE = entries.associateBy { it.code }

        fun fromCodeOrNull(code: QuestionTypeCode): QuestionType? {
            return BY_CODE[code]
        }
    }
}