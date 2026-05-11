package realtime_gateway.infrastructure.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtReactiveAuthenticationManager(
    private val jwtDecoder: ReactiveJwtDecoder,
    private val jwtConverter: CustomJwtAuthenticationConverter
) : ReactiveAuthenticationManager {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val token = authentication.credentials as String

        return runCatching {
            val mv = jwtDecoder.decode(token)
            .map(jwtConverter::convert)
            mv as Mono<Authentication>
        }.onSuccess {
            logger.info("Authentication OK")
        }.getOrElse {
            logger.error("Exception occurred in ${javaClass.simpleName}: ${it.message}", it)
            throw it
        }
    }
}
