package com.xapps.messaging.kafka.events

import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.dto.QuestionAllocationDTO
import com.xapps.questions.contracts.self_test_generation.dto.QuestionDTO
import kotlinx.serialization.Serializable

@Serializable
data class QuestionsRequestedEvent(
    val jobId: JobId,
    val quizId: QuizId,
    val userId: String,

    // Used only for generating...
    val questionCount: Int,
    val allocations: List<QuestionAllocationDTO>,
    val fileKeys: List<String>,
    val quizType: QuizType
)

@Serializable
data class QuestionsGeneratedEvent(
    val jobId: JobId,
    val quizId: QuizId,
    val userId: String,

    val questions: List<QuestionDTO>
)