package realtime_gateway.infrastructure.security

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

// Marks this class as a Spring component so it can be auto-detected and injected where needed
@Component
class JwtAuthEntryPoint : ServerAuthenticationEntryPoint {

    private val logger = LoggerFactory.getLogger(javaClass)
    /**
     * This method is called whenever an unauthenticated user tries to access a secured endpoint.
     * It handles authentication failures and sends a proper HTTP response back to the client.
     *
     * @param exchange Represents the HTTP request-response interaction
     * @param authException The exception thrown because the user is not authenticated
     * @return A Mono<Void> indicating when the response writing is complete
     */
    override fun commence(
        exchange: ServerWebExchange,
        authException: AuthenticationException
    ): Mono<Void> {
        logger.info("Authentication failure in JwtAuthEntryPoint")
        // Get the HTTP response object from the current exchange
        val response = exchange.response

        // Set HTTP status to 401 Unauthorized
        response.statusCode = HttpStatus.UNAUTHORIZED

        // Set the response content type to JSON
        response.headers.contentType = MediaType.APPLICATION_JSON

        // Create a JSON message to return to the client
        val message = """{"error":"Unauthorized. Please login."}"""

        // Wrap the message bytes into a DataBuffer for the response body
        val buffer = response.bufferFactory().wrap(message.toByteArray())

        // Write the buffer to the response and return a Mono to signal completion
        return response.writeWith(Mono.just(buffer))
    }
}