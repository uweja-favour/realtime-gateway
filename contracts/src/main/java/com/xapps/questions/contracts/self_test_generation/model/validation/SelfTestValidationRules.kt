package com.xapps.questions.contracts.self_test_generation.model.validation

import com.xapps.questions.contracts.self_test_generation.model.QuestionGenerationSpec

interface SelfTestValidationRule {
    fun validate(
        state: QuestionGenerationSpec,
        collector: SelfTestValidationResultCollector
    )
}

class CoreMetadataRule : SelfTestValidationRule {

    override fun validate(state: QuestionGenerationSpec, collector: SelfTestValidationResultCollector) {
        if (state.questionCount <= 0) {
            collector.add(
                SelfTestField.NUMBER_OF_QUESTIONS,
                "Total number of questions must be greater than zero."
            )
        }
    }
}

class QuestionAllocationRule : SelfTestValidationRule {

    override fun validate(state: QuestionGenerationSpec, collector: SelfTestValidationResultCollector) {
        val allocations = state.allocations

        if (allocations.isEmpty()) {
            collector.add(
                SelfTestField.QUESTION_ALLOCATIONS,
                "At least one question allocation must be provided."
            )
            return
        }

        val duplicates = allocations
            .groupBy { it.questionType to it.difficulty }
            .filterValues { it.size > 1 }
            .keys

        if (duplicates.isNotEmpty()) {
            val formatted = duplicates.joinToString { (type, diff) -> "$type-$diff" }
            collector.add(
                SelfTestField.QUESTION_ALLOCATIONS,
                "Duplicate question allocations found: $formatted."
            )
        }

        if (allocations.any { it.count <= 0 }) {
            collector.add(
                SelfTestField.QUESTION_ALLOCATIONS,
                "Question counts must be greater than zero."
            )
        }

        val allocatedTotal = allocations.sumOf { it.count }
        if (allocatedTotal != state.questionCount) {
            collector.add(
                SelfTestField.QUESTION_ALLOCATIONS,
                "Sum of question counts ($allocatedTotal) does not match totalQuestions (${state.questionCount})."
            )
        }
    }
}

class TimingRule : SelfTestValidationRule {

    override fun validate(state: QuestionGenerationSpec, collector: SelfTestValidationResultCollector) {

    }
}

class FileSelectionRule : SelfTestValidationRule {

    override fun validate(state: QuestionGenerationSpec, collector: SelfTestValidationResultCollector) {
        if (state.fileKeys.isEmpty()) {
            collector.add(
                SelfTestField.FILES,
                "Select at least 1 file."
            )
        }
    }
}