package realtime_gateway.infrastructure.messaging.websocket.incoming_message.orchestrator

import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import realtime_gateway.infrastructure.messaging.websocket.model.ClientWebSocketMessage
import realtime_gateway.infrastructure.messaging.websocket.incoming_message.processors.ClientWebSocketMessageProcessor
import kotlin.reflect.KClass

@Component
class IncomingWebSocketMessageOrchestrator(
    processors: List<ClientWebSocketMessageProcessor<*>>,
    private val json: Json
) {

    private val processorMap: Map<KClass<out ClientWebSocketMessage>, ClientWebSocketMessageProcessor<*>> =
        processors.associateBy { it.supports() }

    private val logger = LoggerFactory.getLogger(javaClass)

    @Suppress("UNCHECKED_CAST")
    fun dispatch(userId: String, messageJson: String): Mono<Void> {
        return try {
            val message = json.decodeFromString<ClientWebSocketMessage>(messageJson)
            val processor = processorMap[message::class]

            if (processor == null) {
                logger.warn("No processor found for message type: ${message::class.simpleName}")
                return Mono.empty()
            }

            (processor as ClientWebSocketMessageProcessor<ClientWebSocketMessage>)
                .process(userId, message)
        } catch (ex: Exception) {
            logger.error("Error processing message $messageJson", ex)
            Mono.empty()
        }
    }
}