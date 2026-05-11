package com.xapps.model

import kotlin.collections.get

@JvmInline
value class QuizTypeCode(val value: String)

// IMPORTANT: NEVER ALTER THE CODE
enum class QuizType(val code: QuizTypeCode, val displayName: String) {
    CLASSROOM(QuizTypeCode("classroom_quiz_type"), "Classroom Quiz"),
//    EXAM("exam_quiz_type", "Exam Quiz"),
    SELF_TEST(QuizTypeCode("self_test_quiz_type"), "Self Test Quiz");

    companion object {
        private val BY_CODE = entries.associateBy { it.code }

        fun fromCodeOrNull(code: QuizTypeCode?): QuizType? =
            BY_CODE[code]
    }
}