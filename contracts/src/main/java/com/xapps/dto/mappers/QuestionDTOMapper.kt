package com.xapps.dto.mappers

import com.xapps.model.Option
import com.xapps.model.QuestionType
import com.xapps.model.QuizId
import com.xapps.model.question.FibQuestion
import com.xapps.model.question.McQuestion
import com.xapps.model.question.MsQuestion
import com.xapps.model.question.Question
import com.xapps.model.question.TfQuestion
import com.xapps.questions.contracts.self_test_generation.dto.OptionDTO
import com.xapps.questions.contracts.self_test_generation.dto.QuestionDTO

fun QuestionDTO.toQuestion(
    quizId: QuizId,
    number: Int
): Question =
    when(questionType) {
        QuestionType.MC -> McQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            difficulty = difficulty,
            explanation = explanation,
            topic = topic,
            options = options.map { it.toOption() },
            correctOptionId = correctOptionId!!
        )
        QuestionType.MS -> MsQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            difficulty = difficulty,
            explanation = explanation,
            topic = topic,
            options = options.map { it.toOption() },
            correctOptionIds = correctOptionIds.also {
                require(it.isNotEmpty())
            }
        )
        QuestionType.TF -> TfQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            difficulty = difficulty,
            explanation = explanation,
            topic = topic,
            options = options.map { it.toOption() },
            correctOptionId = correctOptionId!!
        )
        QuestionType.FIB -> FibQuestion(
            id = id,
            quizId = quizId,
            number = number,
            text = text,
            difficulty = difficulty,
            explanation = explanation,
            topic = topic,
            acceptableAnswers = acceptableAnswers
        )
    }

private fun OptionDTO.toOption(): Option {
    return Option(
        id = id,
        label = label,
        text = text,
        questionId = questionId
    )
}

