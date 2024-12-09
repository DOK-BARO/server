package kr.kro.dokbaro.server.security.provider

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.security.annotation.CustomAuthenticationProvider
import kr.kro.dokbaro.server.security.authentication.JwtSuccessfulAuthentication
import kr.kro.dokbaro.server.security.authentication.JwtUnauthenticatedToken
import kr.kro.dokbaro.server.security.details.JwtUserDetailsService
import kr.kro.dokbaro.server.security.jwt.access.AccessTokenDecoder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

@CustomAuthenticationProvider
class JwtAuthenticationProvider(
	private val userDetailsService: JwtUserDetailsService,
	private val accessTokenDecoder: AccessTokenDecoder,
) : AuthenticationProvider {
	override fun authenticate(authentication: Authentication): Authentication {
		val certificationId = accessTokenDecoder.decode(authentication.name)

		return JwtSuccessfulAuthentication(userDetailsService.loadUserByUsername(UUIDUtils.uuidToString(certificationId)))
	}

	override fun supports(authentication: Class<*>): Boolean =
		JwtUnauthenticatedToken::class.java.isAssignableFrom(authentication)
}