package com.xapps.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSelfTestQuizRequest(
    val title: String,
    val subject: String,
    val topic: String?,
    val description: String?,
    val questionCount: Int,
    val allocations: List<QuestionAllocationDTO>,
    val files: List<FileUploadDTO>
)

