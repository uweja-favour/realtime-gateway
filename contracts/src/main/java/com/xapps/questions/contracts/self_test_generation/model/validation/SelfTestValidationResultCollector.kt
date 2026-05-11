package com.xapps.questions.contracts.self_test_generation.model.validation

class SelfTestValidationResultCollector {

    private val errors = mutableListOf<SelfTestValidationResult>()

    fun add(field: SelfTestField, message: String) {
        errors += SelfTestValidationResult.of(field, message)
    }

    fun results(): List<SelfTestValidationResult> = errors.toList()
}
