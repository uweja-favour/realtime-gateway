package realtime_gateway.infrastructure.messaging.websocket.handler

import com.xapps.messaging.kafka.events.UserOfflineEvent
import com.xapps.messaging.kafka.events.UserOnlineEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.asFlux
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import realtime_gateway.infrastructure.messaging.kafka.producer.KafkaUserPresenceEventPublisher
import realtime_gateway.infrastructure.messaging.websocket.lifecycle.WebSocketSessionRegistry
import realtime_gateway.infrastructure.messaging.websocket.incoming_message.orchestrator.IncomingWebSocketMessageOrchestrator
import realtime_gateway.infrastructure.messaging.websocket.support.WebSocketTokenExtractor
import realtime_gateway.infrastructure.security.JwtReactiveAuthenticationManager
import realtime_gateway.infrastructure.security.model.DomainUserPrincipal
import realtime_gateway.infrastructure.security.model.JwtAuthenticationToken

@Component
class UserWebSocketHandler(
    private val sessionRegistry: WebSocketSessionRegistry,
    private val kafkaPublisher: KafkaUserPresenceEventPublisher,
    private val authManager: JwtReactiveAuthenticationManager,
    private val incomingWebSocketMessageOrchestrator: IncomingWebSocketMessageOrchestrator,
    private val tokenExtractor: WebSocketTokenExtractor,
    private val json: Json
) : WebSocketHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(session: WebSocketSession): Mono<Void> {
        return authenticate(session)
            .flatMap { userId ->

                val channel = sessionRegistry.register(userId)

                kafkaPublisher.publishUserOnline(UserOnlineEvent(userId))

                val inbound = session.receive()
                    .map { it.payloadAsText }
                    .flatMap { incomingWebSocketMessageOrchestrator.dispatch(userId, it) }
                    .onErrorResume { Mono.empty() }
                    .then()

                val outbound = session.send(
                    channel.consumeAsFlow()
                        .catch { e -> logger.error("Error during send", e) }
                        .map {
                            logger.info("The channel consumed: $it")
                            session.textMessage(it)
                        }
                        .asFlux()
                )

                Mono.`when`(inbound, outbound)
                    .doFinally {
                        sessionRegistry.remove(userId)
                        kafkaPublisher.publishUserOffline(UserOfflineEvent(userId))
                    }
            }
    }


    private fun authenticate(session: WebSocketSession): Mono<String> {
        val token = tokenExtractor.extractToken(session)

        return authManager
            .authenticate(
                JwtAuthenticationToken(token)
            )
            .map { auth ->
                val principal = auth.principal as DomainUserPrincipal
                principal.userId
            }
    }
}