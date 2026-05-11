package com.xapps.questions.contracts.self_test_generation.dto

import com.xapps.model.Difficulty
import com.xapps.model.QuestionType
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val id: String,
    val text: String,
    val explanation: String?,
    val topic: String,
    val difficulty: Difficulty,      // from shared library
    val questionType: QuestionType,  // from shared library
    val options: List<OptionDTO>,
    val correctOptionId: String?,
    val correctOptionIds: Set<String>,
    val acceptableAnswers: Set<String>
)

@Serializable
data class OptionDTO(
    val id: String,
    val questionId: String,
    val label: String,
    val text: String
)
