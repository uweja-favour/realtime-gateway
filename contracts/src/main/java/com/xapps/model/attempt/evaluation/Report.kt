package com.xapps.model.attempt.evaluation

import kotlinx.serialization.Serializable

@Serializable
data class TopicAnalysis(
    val id: String,
    val reportId: String,
    val topic: String,

//    Explanation:
//    0 → user was not confident at all on answered questions
//    100 → user was fully confident on all answered questions
    val averageConfidence: Int,

//    Explanation:
//    0.0 → no correct answers in the topic
//    1.0 → all questions in the topic answered correctly
    val accuracy: Double, // 0.0 to 1.0
    val questionCount: Int,
    val correctCount: Int,
)

@Serializable
data class Report(
    val id: String,
    val evaluationId: String,
//    Explanation:
//    0.0 → the user got all questions wrong
//    1.0 → the user got all questions correct
//    0.0 to 1.0 (represents 0% to 100% correct)
    val overallAccuracy: Double,

//    Explanation:
//    0 → the user reported zero confidence for all answered questions
//    100 → the user reported full confidence for all answered questions
    val overallConfidence: Int,

    val topicAnalysis: List<TopicAnalysis>,
)