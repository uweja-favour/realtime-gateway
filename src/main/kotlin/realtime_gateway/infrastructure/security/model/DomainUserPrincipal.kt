package realtime_gateway.infrastructure.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class DomainUserPrincipal(
    val userId: String,
    val email: String,
    val userRole: UserRole,
    val roles: List<String>
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        roles.map { SimpleGrantedAuthority("ROLE_$it") }

    override fun getPassword(): String? = null
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

enum class UserRole {
    ADMIN, USER
}