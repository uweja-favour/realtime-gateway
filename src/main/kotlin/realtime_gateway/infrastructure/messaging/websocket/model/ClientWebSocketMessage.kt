package realtime_gateway.infrastructure.messaging.websocket.model

import com.xapps.model.QuizId
import kotlinx.serialization.*

@Serializable
sealed class ClientWebSocketMessage {
    // REPRESENTS CLIENT SENT MESSAGES

    @Serializable
    @SerialName("join_quiz")
    data class JoinQuiz(
        val quizId: QuizId
    ) : ClientWebSocketMessage()

    @Serializable
    @SerialName("submit_answer")
    data class SubmitAnswer(
        val quizId: QuizId,
        val answer: String
    ) : ClientWebSocketMessage()

    @Serializable
    @SerialName("ping")
    data object Ping : ClientWebSocketMessage()
}