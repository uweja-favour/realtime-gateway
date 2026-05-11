package com.xapps.questions.contracts

import com.xapps.questions.contracts.self_test_generation.model.QuestionGenerationSpec

interface QuizGenerationSpecValidator<R : ValidationResult> {
    fun validate(state: QuestionGenerationSpec): List<R>
}