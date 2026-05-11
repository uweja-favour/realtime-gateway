package com.xapps.messaging.kafka.events

import com.xapps.model.QuizId
import kotlinx.serialization.Serializable

@Serializable
data class SelfTestQuizDeliveredEvent(
    val userId: String,
    val quizIds: List<QuizId>
)