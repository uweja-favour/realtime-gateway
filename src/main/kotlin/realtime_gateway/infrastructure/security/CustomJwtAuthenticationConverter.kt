package realtime_gateway.infrastructure.security

import org.slf4j.LoggerFactory
import realtime_gateway.infrastructure.security.jwt.JwtClaims
import realtime_gateway.infrastructure.security.model.DomainJwtAuthenticationToken
import realtime_gateway.infrastructure.security.model.DomainUserPrincipal
import realtime_gateway.infrastructure.security.model.UserRole
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class CustomJwtAuthenticationConverter : Converter<Jwt, AbstractAuthenticationToken> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val principal = buildPrincipal(jwt)
        return DomainJwtAuthenticationToken(
            jwt = jwt,
            authorities = principal.authorities,
            principal = principal
        )
    }

    private fun buildPrincipal(jwt: Jwt): DomainUserPrincipal {
        val userId = extractUserId(jwt)
        val email = extractEmail(jwt)
        val roles = extractRoles(jwt)
        val userRole = determinePrimaryRole(roles)

        return DomainUserPrincipal(
            userId = userId,
            email = email,
            userRole = userRole,
            roles = roles
        ).also {
            logger.info("DomainUserPrincipal: $it")
        }
    }

    private fun extractUserId(jwt: Jwt): String =
        jwt.claims[JwtClaims.SUBJECT] as? String
            ?: throw IllegalArgumentException("JWT does not contain a user identifier")

    private fun extractEmail(jwt: Jwt): String =
        jwt.claims[JwtClaims.EMAIL] as? String
            ?: throw IllegalArgumentException("JWT does not contain an email claim")

    private fun extractRoles(jwt: Jwt): List<String> {
        val primaryRole = (jwt.claims[JwtClaims.ROLE] as? String)
            ?: JwtClaims.DEFAULT_ROLE

        val additionalRoles = jwt.claims[JwtClaims.ROLES]
            ?.let { it as? Collection<*> }
            ?.filterIsInstance<String>()
            .orEmpty()

        return (listOf(primaryRole) + additionalRoles)
            .map { it.uppercase() }
            .distinct()
    }

    private fun determinePrimaryRole(roles: List<String>): UserRole =
        roles.firstNotNullOfOrNull {
            runCatching { UserRole.valueOf(it) }.getOrNull()
        } ?: UserRole.USER
}
