@file: OptIn(ExperimentalTime::class)

package com.xapps.model

import com.xapps.time.types.KotlinInstant
import kotlin.time.ExperimentalTime

// General Quiz Contract.
// Every other quiz is a child of this quiz contract and overrides its parameters
interface QuizContract {
    val id: String
    val title: String // compulsory
    val subject: String // compulsory
    val topic: String? // optional
    val description: String? // optional
    val questions: List<QuestionContract>
    val quizType: QuizType

    val createdAt: KotlinInstant
}

interface QuestionContract {
    val id: String
    val number: Int
    val text: String
    val difficulty: Difficulty
    val questionType: QuestionType
}

interface OptionContract {
    val id: String
    /** Human-readable display label (A, B, C, i, ii, etc.) */
    val label: String

    /** The option text shown to users */
    val text: String
}

typealias QuizId = String
typealias QuestionId = String
typealias OptionId = String

