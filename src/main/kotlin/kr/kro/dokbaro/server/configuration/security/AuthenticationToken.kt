package kr.kro.dokbaro.server.configuration.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class AuthenticationToken(
	val userId: String,
	val roles: Collection<String>,
) : AbstractAuthenticationToken(
		roles.map { SimpleGrantedAuthority(it) },
	) {
	override fun getCredentials(): Any? = null

	override fun getPrincipal(): Any = userId
}