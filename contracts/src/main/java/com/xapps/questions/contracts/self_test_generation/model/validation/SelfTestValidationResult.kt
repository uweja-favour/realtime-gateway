package com.xapps.questions.contracts.self_test_generation.model.validation

import com.xapps.questions.contracts.ValidationResult

data class SelfTestValidationResult(
    val field: SelfTestField,
    val message: String? = null
) : ValidationResult {
    companion object {
        fun of(
            field: SelfTestField,
            message: String? = null
        ) = SelfTestValidationResult(field, message)
    }
}

enum class SelfTestField {
    NUMBER_OF_QUESTIONS,
    QUESTION_ALLOCATIONS,

    TIMING,
    DIFFICULTY,
    FILES
}