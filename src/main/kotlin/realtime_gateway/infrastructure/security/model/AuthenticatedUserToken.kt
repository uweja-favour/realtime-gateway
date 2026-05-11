package realtime_gateway.infrastructure.security.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

// Represents an AUTHENTICATED user
class AuthenticatedUserToken(
    private val principal: Any,
    authorities: Collection<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {

    init {
        super.setAuthenticated(true)
    }

    override fun getCredentials(): Any? = null
    override fun getPrincipal(): Any = principal
}
