package realtime_gateway.infrastructure.messaging.websocket.support

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.util.UriComponentsBuilder

@Component
class WebSocketTokenExtractor {

    fun extractToken(session: WebSocketSession): String {
        val authHeader = session.handshakeInfo.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?: throw IllegalArgumentException("Missing Authorization header in WebSocket handshake")

        return authHeader
            .takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?: throw IllegalArgumentException("Invalid Authorization header format")
    }
}