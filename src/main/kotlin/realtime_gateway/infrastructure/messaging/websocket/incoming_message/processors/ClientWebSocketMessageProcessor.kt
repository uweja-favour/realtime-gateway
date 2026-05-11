package realtime_gateway.infrastructure.messaging.websocket.incoming_message.processors

import reactor.core.publisher.Mono
import realtime_gateway.infrastructure.messaging.websocket.model.ClientWebSocketMessage
import kotlin.reflect.KClass

interface ClientWebSocketMessageProcessor<T : ClientWebSocketMessage> {

    fun supports(): KClass<T>

    fun process(
        userId: String,
        message: T
    ): Mono<Void>
}