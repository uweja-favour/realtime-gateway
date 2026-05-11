package com.xapps.questions.contracts.question_generation

import com.xapps.model.Difficulty
import com.xapps.model.QuestionType
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAllocation(
    val id: String,
    val specId: String,
    val questionType: QuestionType,
    val difficulty: Difficulty,
    val count: Int
) {
    override fun toString(): String {
        return buildString {
            appendLine("Question Type: ${questionType.name}")
            appendLine("Difficulty: ${difficulty.name}")
            appendLine("Count: $count")
        }
    }
}
//
//@Serializable
//data class QuestionAllocation(
//    val questionType: QuestionType,
//    val difficulty: Difficulty,
//    val count: Int
//) {
//    override fun toString(): String {
//        return buildString {
//            appendLine("Question Type: ${questionType.name}")
//            appendLine("Difficulty: ${difficulty.name}")
//            appendLine("Count: $count")
//        }
//    }
//}