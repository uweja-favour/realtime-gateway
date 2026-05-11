package realtime_gateway.infrastructure.security

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class BearerTokenExtractor {

    fun extractBearerToken(request: ServerHttpRequest): String? {
        request.headers.getFirst("Authorization")
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            .let { return it }
    }
}