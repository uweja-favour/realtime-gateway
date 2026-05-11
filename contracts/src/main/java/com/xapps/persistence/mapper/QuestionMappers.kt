package com.xapps.persistence.mapper

import com.xapps.model.Difficulty
import com.xapps.model.question.FibQuestion
import com.xapps.model.question.McQuestion
import com.xapps.model.question.MsQuestion
import com.xapps.model.question.Question
import com.xapps.model.question.TfQuestion
import com.xapps.persistence.FibQuestionDocument
import com.xapps.persistence.McQuestionDocument
import com.xapps.persistence.MsQuestionDocument
import com.xapps.persistence.QuestionDocument
import com.xapps.persistence.TfQuestionDocument

fun QuestionDocument.toDomain(): Question {
    val difficulty = Difficulty.fromCodeOrNull(difficultyCode)
        ?: error("Unknown difficulty code: $difficultyCode")

    return when (this) {
        is McQuestionDocument -> McQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficulty = difficulty,
            options = options.map { it.toDomain() },
            correctOptionId = correctOptionId
        )

        is MsQuestionDocument -> MsQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficulty = difficulty,
            options = options.map { it.toDomain() },
            correctOptionIds = correctOptionIds
        )

        is TfQuestionDocument -> TfQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficulty = difficulty,
            options = options.map { it.toDomain() },
            correctOptionId = correctOptionId
        )

        is FibQuestionDocument -> FibQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficulty = difficulty,
            acceptableAnswers = acceptableAnswers
        )
    }
}


fun Question.toDocument(): QuestionDocument {
    val difficultyCode = difficulty.code

    return when (this) {

        is McQuestion -> McQuestionDocument(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficultyCode = difficultyCode,
            options = options.map { it.toDocument() },
            correctOptionId = correctOptionId
        )

        is MsQuestion -> MsQuestionDocument(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficultyCode = difficultyCode,
            options = options.map { it.toDocument() },
            correctOptionIds = correctOptionIds
        )

        is TfQuestion -> TfQuestionDocument(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficultyCode = difficultyCode,
            options = options.map { it.toDocument() },
            correctOptionId = correctOptionId
        )

        is FibQuestion -> FibQuestionDocument(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            explanation = explanation,
            topic = topic,
            difficultyCode = difficultyCode,
            acceptableAnswers = acceptableAnswers
        )
    }
}