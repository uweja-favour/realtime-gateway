package com.xapps.dto

import com.xapps.dto.job.JobStatus
import com.xapps.questions.contracts.question_generation.JobId
import kotlinx.serialization.Serializable

@Serializable
data class SseJobUpdateDto(
    val jobId: JobId,
    val status: JobStatus
)

