package realtime_gateway.infrastructure.messaging.websocket.lifecycle

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Sinks
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionRegistry {
    private val sessions = ConcurrentHashMap<String, Channel<String>>()

    fun register(userId: String): Channel<String> {
        val channel = Channel<String>(Channel.BUFFERED)
        sessions[userId] = channel
        return channel
    }

    fun get(userId: String): Channel<String>? = sessions[userId]

    fun remove(userId: String) {
        sessions.remove(userId)?.close()
    }
}