package realtime_gateway.infrastructure.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import java.nio.charset.StandardCharsets
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtDecoderConfig {

    @Bean
    fun reactiveJwtDecoder(
        @Value("\${security.jwt.accessSecret}") accessSecret: String
    ): ReactiveJwtDecoder {
        require(!accessSecret.isNullOrBlank()) {
            "security.jwt.accessSecret must be configured"
        }

        val key = SecretKeySpec(
            accessSecret.toByteArray(StandardCharsets.UTF_8),
            "HmacSHA256"
        )

        return NimbusReactiveJwtDecoder
            .withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS256)
            .build()
    }
}
