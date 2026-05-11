@file:OptIn(ExperimentalSerializationApi::class)
package realtime_gateway.infrastructure.messaging.websocket.model

import com.xapps.model.QuizId
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerWebSocketMessage {
    // REPRESENTS SERVER SENT MESSAGES

    @Serializable
    @SerialName("new_self_test_quiz_payload")
    data class NewSelfTestQuizPayload(
        val quizIds: List<QuizId>
    ) : ServerWebSocketMessage()

    @Serializable
    @SerialName("new_classroom_quiz_payload")
    data class NewClassroomQuizPayload(
        val quizIds: List<QuizId>
    ) : ServerWebSocketMessage()

    @Serializable
    @SerialName("new_note_summary_payload")
    data class NewNoteSummaryPayload(
        val noteSummaryIds: List<String>
    ) : ServerWebSocketMessage()

    @Serializable
    @SerialName("ping_client")
    data object PingClient : ServerWebSocketMessage()

    /**
     * Represents a server-initiated instruction for a tutor client to refresh
     * an existing classroom quiz.
     *
     * <p>
     * This message is emitted when the state of a classroom quiz changes after
     * initial delivery. Typical triggers include:
     * - A participant submitting an attempt
     * - A participant joining the quiz
     * - The submission deadline being reached
     *
     * <p>
     * The payload contains only the quiz ID. The client is expected to use this ID
     * to fetch the latest quiz state from the classroom quiz service.
     *
     * @property classroomQuizIds unique identifier of the classroom quizzes that should be refreshed.
     */
    @Serializable
    @SerialName("refresh_tutor_classroom_quiz_payload")
    data class RefreshTutorClassroomQuizPayload(
        val classroomQuizIds: List<QuizId>
    ) : ServerWebSocketMessage()

    @Serializable
    @SerialName("refresh_participant_classroom_quiz_payload")
    data class RefreshParticipantClassroomQuizPayload(
        val classroomQuizId: QuizId
    ) : ServerWebSocketMessage()

    @Serializable
    @SerialName("refresh_participant_classroom_quizzes_payload")
    data class RefreshParticipantClassroomQuizzesPayload(
        val classroomQuizIds: List<QuizId>
    ) : ServerWebSocketMessage()

}
