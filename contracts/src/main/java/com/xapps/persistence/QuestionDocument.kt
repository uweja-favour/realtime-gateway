package com.xapps.persistence

import com.xapps.model.DifficultyCode

sealed class QuestionDocument {
    abstract val id: String
    abstract val quizId: String
    abstract val number: Int
    abstract val text: String
    abstract val explanation: String?
    abstract val topic: String
    abstract val difficultyCode: DifficultyCode
}

data class McQuestionDocument(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficultyCode: DifficultyCode,

    val options: List<OptionDocument>,
    val correctOptionId: String
) : QuestionDocument()

data class MsQuestionDocument(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficultyCode: DifficultyCode,

    val options: List<OptionDocument>,
    val correctOptionIds: Set<String>
) : QuestionDocument()

data class TfQuestionDocument(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficultyCode: DifficultyCode,

    val options: List<OptionDocument>,
    val correctOptionId: String
) : QuestionDocument()

data class FibQuestionDocument(
    override val id: String,
    override val quizId: String,
    override val number: Int,
    override val text: String,
    override val explanation: String?,
    override val topic: String,
    override val difficultyCode: DifficultyCode,

    val acceptableAnswers: Set<String>
) : QuestionDocument()

