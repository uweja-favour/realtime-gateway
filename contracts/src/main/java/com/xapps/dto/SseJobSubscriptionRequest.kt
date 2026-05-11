package com.xapps.dto

import com.xapps.questions.contracts.question_generation.JobId
import kotlinx.serialization.Serializable

@Serializable
data class SseJobSubscriptionRequest(
    val jobIds: Set<JobId>
)