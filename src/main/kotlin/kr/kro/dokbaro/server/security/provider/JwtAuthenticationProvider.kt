package kr.kro.dokbaro.server.security.provider

import kr.kro.dokbaro.server.security.annotation.CustomAuthenticationProvider
import kr.kro.dokbaro.server.security.details.JwtUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

@CustomAuthenticationProvider
class JwtAuthenticationProvider(
	private val jwtUserDetailsService: JwtUserDetailsService,
) : AuthenticationProvider {
	override fun authenticate(authentication: Authentication): Authentication {
		TODO("Not yet implemented")
	}

	override fun supports(authentication: Class<*>): Boolean {
		TODO("Not yet implemented")
	}
}