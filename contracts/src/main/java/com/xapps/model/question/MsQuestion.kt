package com.xapps.model.question

import com.xapps.model.Difficulty
import com.xapps.model.MsQuestionContract
import com.xapps.model.Option
import com.xapps.model.QuestionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("MsQuestion")
data class MsQuestion(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficulty: Difficulty,
    override val options: List<Option>,
    val correctOptionIds: Set<String>
) : Question(QuestionType.MS), MsQuestionContract