package com.xapps.questions.contracts.self_test_generation.model

import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.questions.contracts.question_generation.QuestionAllocation
import kotlinx.serialization.Serializable

@Serializable
data class QuestionGenerationSpec(
    val id: String,
    val userId: String,
    val quizId: QuizId,
    val questionCount: Int,
    val allocations: List<QuestionAllocation>,
    val fileKeys: List<String>,
    val jobId: JobId,
    val quizType: QuizType
)