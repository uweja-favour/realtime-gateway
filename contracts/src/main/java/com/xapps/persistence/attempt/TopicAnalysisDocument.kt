package com.xapps.persistence.attempt

data class TopicAnalysisDocument(
    val id: String,
    val reportId: String,
    val topic: String,
    val averageConfidence: Int,
    val accuracy: Double,
    val questionCount: Int,
    val correctCount: Int
)