package realtime_gateway.infrastructure.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken

// Represents an UNAUTHENTICATED JWT request
class JwtAuthenticationToken(
    private val token: String
) : AbstractAuthenticationToken(emptyList()) {

    override fun getCredentials(): Any = token
    override fun getPrincipal(): Any = token

    override fun isAuthenticated(): Boolean = false
}



















//class DomainJwtAuthenticationToken(
//    private val jwt: Jwt,
//    authorities: Collection<GrantedAuthority>,
//    private val principal: DomainUserPrincipal
//) : AbstractAuthenticationToken(authorities) {
//
//    init {
//        isAuthenticated = true
//    }
//
//    override fun getCredentials(): Any = jwt
//
//    override fun getPrincipal(): Any = principal
//}
