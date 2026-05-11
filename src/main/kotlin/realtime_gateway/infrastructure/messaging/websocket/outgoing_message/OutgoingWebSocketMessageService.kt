package realtime_gateway.infrastructure.messaging.websocket.outgoing_message

import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import realtime_gateway.infrastructure.messaging.websocket.lifecycle.WebSocketSessionRegistry
import realtime_gateway.infrastructure.messaging.websocket.model.ServerWebSocketMessage

@Service
class OutgoingWebSocketMessageService(
    private val sessionRegistry: WebSocketSessionRegistry,
    private val json: Json
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun sendToUser(
        userId: String,
        payload: ServerWebSocketMessage
    ) {

        val channel = sessionRegistry.get(userId)
            ?: run {
                log.info("No active channel for userId={}", userId)
                return
            }

        val jsonString = json.encodeToString(
            ServerWebSocketMessage.serializer(),
            payload
        )

        log.info("About to send: $jsonString to user: $userId")

        val result = channel.trySendBlocking(jsonString)

        if (result.isFailure) {
            log.error("Failed sending websocket message to userId={}", userId)
        } else log.info("Successfully sent it???")
    }
}