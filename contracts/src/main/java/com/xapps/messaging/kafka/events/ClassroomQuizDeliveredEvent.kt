package com.xapps.messaging.kafka.events

import com.xapps.model.QuizId
import kotlinx.serialization.Serializable

@Serializable
data class ClassroomQuizDeliveredEvent(
    val tutorId: String,
    val quizIds: List<QuizId>
)