package com.xapps.model.question

import com.xapps.model.Difficulty
import com.xapps.model.McQuestionContract
import com.xapps.model.Option
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("McQuestion")
data class McQuestion(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficulty: Difficulty,
    override val options: List<Option>,
    val correctOptionId: String
) : Question(QuestionType.MC), McQuestionContract