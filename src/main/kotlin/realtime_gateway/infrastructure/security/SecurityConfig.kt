package realtime_gateway.infrastructure.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val jwtAuthEntryPoint: JwtAuthEntryPoint,
    private val authServiceWebFilter: AuthServiceWebFilter,
    @Value("\${security.auth.enabled:true}")
    private val authEnabled: Boolean
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }

            .exceptionHandling {
                it.authenticationEntryPoint { exchange, ex ->
                    jwtAuthEntryPoint.commence(exchange, ex)
                }
            }

            // Stateless API
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

            .authorizeExchange {
                // Allow websocket handshake endpoint
                it.pathMatchers("/api/v1/ws/**").permitAll()
                it.anyExchange().authenticated()

//                if (!authEnabled) {
//                    it.anyExchange().permitAll()
//                } else {
//                    it.anyExchange().authenticated()
//                }
            }

            .httpBasic { it.disable() }
            .formLogin { it.disable() }

            // Custom authentication filter
            .addFilterAt(
                authServiceWebFilter,
                SecurityWebFiltersOrder.AUTHENTICATION
            )

            .build()
    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val config = CorsConfiguration().apply {
//            allowedOrigins = listOf("http://10.0.2.2:5177", "*")
//            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            allowedHeaders = listOf("*")
//            allowCredentials = true
//        }
//
//        return UrlBasedCorsConfigurationSource().apply {
//            registerCorsConfiguration("/**", config)
//        }
//    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val config = CorsConfiguration().apply {

            allowedOriginPatterns = listOf("*")

            allowedMethods = listOf(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
            )

            allowedHeaders = listOf("*")

            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}
