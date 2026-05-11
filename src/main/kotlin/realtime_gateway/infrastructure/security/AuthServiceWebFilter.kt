package realtime_gateway.infrastructure.security

import realtime_gateway.infrastructure.security.model.JwtAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
//@Order(Ordered.LOWEST_PRECEDENCE)
class AuthServiceWebFilter(
    private val authenticationManager: JwtReactiveAuthenticationManager,
    private val tokenExtractor: BearerTokenExtractor,
    @Value("\${security.auth.enabled:true}")
    private val authEnabled: Boolean
) : WebFilter {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        log.info("beginning filter in $javaClass")

        if (!authEnabled) {
            log.error("Development mode: bypassing authentication")
            return chain.filter(exchange)
        }

        log.info("About to extract token...")
        val token = tokenExtractor.extractBearerToken(exchange.request)
            ?: return chain.filter(exchange)

        log.info("Token extracted: $token")

        val authRequest = JwtAuthenticationToken(token)

        log.info("Proceeding to authenticate...")
        return authenticationManager.authenticate(authRequest)
            .flatMap { authenticated ->
                chain.filter(exchange)
                    .contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(authenticated)
                    )
            }
            .onErrorResume {
                log.error("An error occurred in authentication: ${it.message}", it)
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                exchange.response.setComplete()
            }
    }
}
