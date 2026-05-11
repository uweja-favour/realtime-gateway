package realtime_gateway.infrastructure.messaging.kafka.consumer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.ClassroomQuizStateRefreshEvent
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import realtime_gateway.infrastructure.messaging.kafka.config.KafkaBroadcastConsumerConfig
import realtime_gateway.infrastructure.messaging.websocket.model.ServerWebSocketMessage
import realtime_gateway.infrastructure.messaging.websocket.outgoing_message.OutgoingWebSocketMessageService

/**
 * Kafka consumer responsible for notifying tutors when an existing classroom quiz
 * has undergone a state change that requires client-side refresh.
 *
 * <p>
 * This consumer listens for {@link ClassroomQuizStateChangedEvent} events, which
 * represent mutations to an already delivered classroom quiz.
 *
 * <p>
 * Upon receiving such an event, this component:
 * - Extracts the affected tutor ID and quiz ID
 * - Sends a lightweight WebSocket message instructing the tutor's device
 *   to refresh the quiz from the classroom quiz service
 *
 * <p>
 * This class does not transmit quiz data. It only signals that previously fetched
 * data is now stale and should be reloaded.
 */
@Component
class ClassroomQuizStateRefreshNotificationConsumer(
    private val outgoingWebSocketMessageService: OutgoingWebSocketMessageService,
    private val compressionService: ObjectCompressionService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Handles incoming Kafka messages indicating that a classroom quiz state has changed.
     *
     * <p>
     * This method:
     * - Deserializes the raw JSON payload into a {@link ClassroomQuizStateChangedEvent}
     * - Asynchronously triggers a WebSocket refresh notification to the tutor
     *
     * @param rawEventPayload JSON string representing the state change event.
     */
    @KafkaListener(
        topics = [KafkaTopics.Quiz.CLASSROOM_STATE_CHANGED],
        containerFactory = KafkaBroadcastConsumerConfig.BROADCAST_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    fun handleClassroomQuizStateChangedEvent(payload: ByteArray) {

        log.info("Received ClassroomQuizStateChangedEvent: $payload")

        val stateChangedEvent = compressionService.decompress(
            ClassroomQuizStateRefreshEvent.serializer(),
            payload
        )

        CoroutineScope(Dispatchers.IO).launch {
            when(stateChangedEvent) {
                is ClassroomQuizStateRefreshEvent.Tutor -> {
                    notifyTutorToRefreshQuiz(stateChangedEvent)
                }

                is ClassroomQuizStateRefreshEvent.QuizRefreshParticipants -> {
                    notifyParticipantsToRefreshQuiz(stateChangedEvent)
                }

                is ClassroomQuizStateRefreshEvent.ParticipantRefreshQuizzes -> {
                    notifyParticipantsToRefreshQuiz(stateChangedEvent)
                }
            }
        }
    }

    private suspend fun notifyParticipantsToRefreshQuiz(event: ClassroomQuizStateRefreshEvent.QuizRefreshParticipants) =
        supervisorScope {
            event.participantIds.forEach { participantId ->
                launch {
                    outgoingWebSocketMessageService.sendToUser(
                        userId = participantId,
                        payload = ServerWebSocketMessage.RefreshParticipantClassroomQuizPayload(
                            classroomQuizId = event.classroomQuizId
                        )
                    )
                }
            }
        }

    private fun notifyParticipantsToRefreshQuiz(event: ClassroomQuizStateRefreshEvent.ParticipantRefreshQuizzes) =
        outgoingWebSocketMessageService.sendToUser(
            userId = event.participantId,
            payload = ServerWebSocketMessage.RefreshParticipantClassroomQuizzesPayload(
                classroomQuizIds = event.classroomQuizIds
            )
        )

    /**
     * Sends a WebSocket message instructing the tutor client to refresh
     * a specific classroom quiz.
     *
     * <p>
     * The client is expected to use the provided quiz ID to fetch the updated
     * quiz state from the backend service.
     *
     * @param event the domain event containing tutor ID and affected quiz ID.
     */
    private fun notifyTutorToRefreshQuiz(event: ClassroomQuizStateRefreshEvent.Tutor) {
        outgoingWebSocketMessageService.sendToUser(
            userId = event.tutorId,
            payload = ServerWebSocketMessage.RefreshTutorClassroomQuizPayload(classroomQuizIds = event.classroomQuizIds)
        )
    }
}