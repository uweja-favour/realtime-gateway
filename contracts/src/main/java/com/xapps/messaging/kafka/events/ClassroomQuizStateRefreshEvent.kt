package com.xapps.messaging.kafka.events

import com.xapps.model.QuizId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClassroomQuizStateRefreshEvent {

    @Serializable
    @SerialName("TutorQuizStateRefreshEvent")
    data class Tutor(
        val tutorId: String,
        val classroomQuizIds: List<QuizId>
    ): ClassroomQuizStateRefreshEvent

    @Serializable
    @SerialName("ParticipantRefreshQuizzes")
    data class ParticipantRefreshQuizzes(
        val participantId: String,
        val classroomQuizIds: List<QuizId>
    ) : ClassroomQuizStateRefreshEvent

    @Serializable
    @SerialName("QuizRefreshParticipants")
    data class QuizRefreshParticipants(
        val classroomQuizId: QuizId,
        val participantIds: List<String>
    ) : ClassroomQuizStateRefreshEvent
}