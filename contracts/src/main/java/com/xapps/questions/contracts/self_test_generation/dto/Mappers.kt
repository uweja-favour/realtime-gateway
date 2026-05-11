package com.xapps.questions.contracts.self_test_generation.dto

import com.xapps.dto.QuestionAllocationDTO
import com.xapps.model.QuizId
import com.xapps.model.QuizType
import com.xapps.questions.contracts.question_generation.JobId
import com.xapps.questions.contracts.question_generation.QuestionAllocation
import com.xapps.questions.contracts.self_test_generation.model.QuestionGenerationSpec

private fun QuestionAllocationDTO.toDomain(
    id: String,
    specId: String
): QuestionAllocation =
    QuestionAllocation(
        id = id,
        specId = specId,
        questionType = type,
        difficulty = difficulty,
        count = count
    )

fun createSelfTestQuestionGenerationSpec(
    userId: String,
    quizId: QuizId,
    jobId: JobId,
    questionCount: Int,
    fileKeys: List<String>,
    allocations: List<QuestionAllocationDTO>,
    quizType: QuizType,
    idGenerator: () -> String,
): QuestionGenerationSpec {
    val specId = idGenerator()
    return QuestionGenerationSpec(
        id = specId,
        userId = userId,
        quizId = quizId,
        questionCount = questionCount,
        allocations = allocations.map {
            it.toDomain(
                id = idGenerator(),
                specId = specId
            )
        },
        fileKeys = fileKeys,
        jobId = jobId,
        quizType = quizType
    )
}

