package com.xapps.model.question

import com.xapps.model.Difficulty
import com.xapps.model.FibQuestionContract
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FibQuestion")
data class FibQuestion(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficulty: Difficulty,
    val acceptableAnswers: Set<String>
) : Question(QuestionType.FIB), FibQuestionContract