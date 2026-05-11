package realtime_gateway.infrastructure.messaging.kafka.consumer

import com.xapps.messaging.kafka.KafkaTopics
import com.xapps.messaging.kafka.events.SelfTestQuizDeliveredEvent
import com.xapps.platform.core.compression.ObjectCompressionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import realtime_gateway.infrastructure.messaging.kafka.config.KafkaBroadcastConsumerConfig
import realtime_gateway.infrastructure.messaging.websocket.model.ServerWebSocketMessage
import realtime_gateway.infrastructure.messaging.websocket.outgoing_message.OutgoingWebSocketMessageService

@Component
class SelfTestQuizGenerationNotificationConsumer(
    private val outgoingWebSocketMessageService: OutgoingWebSocketMessageService,
    private val compressionService: ObjectCompressionService,
) {

    // This is called to deliver a ServerWebSocketMessage.NewSelfTestQuizPayload to a user when he comes online.

    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(
        topics = [KafkaTopics.Quiz.SELF_TEST_PAYLOAD],
        containerFactory = KafkaBroadcastConsumerConfig.BROADCAST_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    fun handleSelfTestQuizGeneratedEvent(payload: ByteArray) {

        log.info("Received SelfTestQuizDeliveredEvent: $payload")

        val selfTestQuizDeliveredEvent = compressionService.decompress(
            SelfTestQuizDeliveredEvent.serializer(),
            payload
        )

        CoroutineScope(Dispatchers.IO).launch {
            notifyUserQuizIsReady(selfTestQuizDeliveredEvent)
        }
    }

    private suspend fun notifyUserQuizIsReady(event: SelfTestQuizDeliveredEvent) {
        outgoingWebSocketMessageService.sendToUser(
            userId = event.userId,
            payload = ServerWebSocketMessage.NewSelfTestQuizPayload(event.quizIds)
        )
    }
}

