package com.xapps.persistence.attempt

data class ReportDocument(
    val id: String,
    val evaluationId: String,
    val overallAccuracy: Double,
    val overallConfidence: Int,
    val topicAnalysis: List<TopicAnalysisDocument>
)