package com.xapps.model.question

import com.xapps.model.Difficulty
import com.xapps.model.QuestionContract
import com.xapps.model.QuestionType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


//
//// For Exam Quiz, each question must have question difficulty, category and topic
//// Topic vs Category
////
////Topic: Fine-grained, specific to a single question.
////
////Example:
////Question: “Solve for x: 2x + 3 = 7”
////
////Topic = "Linear Equations".
////
////
////Category: Higher-level grouping (umbrella over multiple topics).
////
////Example:
////Category = "Algebra".
////
////Topics under it: Linear Equations, Quadratic Equations, Polynomials.

@OptIn(ExperimentalSerializationApi::class)
@Serializable
sealed class Question(override val questionType: QuestionType) : QuestionContract {
    abstract override val id: String
    abstract val quizId: String
    abstract override val number: Int
    abstract override val text: String
    abstract override val difficulty: Difficulty
    abstract val explanation: String?
    abstract val topic: String
}

public fun Question.type(): QuestionType = when(this) {
    is McQuestion -> QuestionType.MC
    is MsQuestion -> QuestionType.MS
    is FibQuestion -> QuestionType.FIB
    is TfQuestion -> QuestionType.TF
}