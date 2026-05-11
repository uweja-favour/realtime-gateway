package realtime_gateway.infrastructure.messaging.websocket.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import realtime_gateway.infrastructure.messaging.websocket.handler.UserWebSocketHandler

@Configuration
class WebSocketConfig(
    private val handler: UserWebSocketHandler
) {

    @Bean
    fun webSocketHandlerMapping(): HandlerMapping {
        val map = mapOf(
            "/api/v1/ws/realtime" to handler,
            "/api/v1/wss/realtime" to handler,
        )
        // the url -> ws://10.0.2.2:8084/api/v1/ws/realtime?token=JWT

        return SimpleUrlHandlerMapping(map).apply {
            order = -1
        }
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter =
        WebSocketHandlerAdapter()
}