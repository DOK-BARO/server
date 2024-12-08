package kr.kro.dokbaro.server.security.provider

import kr.kro.dokbaro.server.security.annotation.CustomAuthenticationProvider
import kr.kro.dokbaro.server.security.details.EmailUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

@CustomAuthenticationProvider
class FormAuthenticationProvider(
	private val userDetailsService: EmailUserDetailsService,
	private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {
	override fun authenticate(authentication: Authentication): Authentication {
		val token: UsernamePasswordAuthenticationToken = authentication as UsernamePasswordAuthenticationToken

		val user = userDetailsService.loadUserByUsername(authentication.name)

		if (passwordEncoder.matches(token.credentials.toString(), user.password)) {
			throw BadCredentialsException("Invalid username or password")
		}

		return UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
	}

	override fun supports(authentication: Class<*>): Boolean =
		UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}