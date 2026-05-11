package com.xapps.dto

import com.xapps.model.Difficulty
import com.xapps.model.QuestionType
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAllocationDTO(
    val type: QuestionType,
    val difficulty: Difficulty,
    val count: Int
)