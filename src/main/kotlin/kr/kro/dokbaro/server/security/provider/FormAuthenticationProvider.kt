package kr.kro.dokbaro.server.security.provider

import kr.kro.dokbaro.server.security.annotation.CustomAuthenticationProvider
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

@CustomAuthenticationProvider
class FormAuthenticationProvider : AuthenticationProvider {
	override fun authenticate(authentication: Authentication): Authentication {
		TODO("Not yet implemented")
	}

	override fun supports(authentication: Class<*>): Boolean {
		TODO("Not yet implemented")
	}
}