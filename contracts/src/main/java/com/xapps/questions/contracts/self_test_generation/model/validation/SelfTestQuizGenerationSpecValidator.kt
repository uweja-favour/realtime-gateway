package com.xapps.questions.contracts.self_test_generation.model.validation

import com.xapps.questions.contracts.QuizGenerationSpecValidator
import com.xapps.questions.contracts.self_test_generation.model.QuestionGenerationSpec

private class SelfTestQuizGenerationSpecValidator(
    private val rules: List<SelfTestValidationRule>
): QuizGenerationSpecValidator<SelfTestValidationResult> {

    override fun validate(state: QuestionGenerationSpec): List<SelfTestValidationResult> {
        val collector = SelfTestValidationResultCollector()
        rules.forEach { it.validate(state, collector) }
        return collector.results()
    }

    companion object {
        fun default(): SelfTestQuizGenerationSpecValidator =
            SelfTestQuizGenerationSpecValidator(
                rules = listOf(
                    CoreMetadataRule(),
                    QuestionAllocationRule(),
                    TimingRule(),
                    FileSelectionRule()
                )
            )
    }
}