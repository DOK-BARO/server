package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import kr.kro.dokbaro.server.security.filter.JwtValidationFilter
import kr.kro.dokbaro.server.security.filter.OAuth2AuthenticationRedirectSetUpFilter
import kr.kro.dokbaro.server.security.jwt.JwtHttpCookieInjector
import kr.kro.dokbaro.server.security.jwt.JwtTokenReGenerator
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@HttpSecurityChain
class AuthorizeFilterChain(
	private val authenticationManager: AuthenticationManager,
	private val jwtTokenReGenerator: JwtTokenReGenerator,
	private val jwtHttpCookieInjector: JwtHttpCookieInjector,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin
			.addFilterBefore(
				OAuth2AuthenticationRedirectSetUpFilter(),
				OAuth2AuthorizationRequestRedirectFilter::class.java,
			).addFilterBefore(
				JwtValidationFilter(authenticationManager, jwtTokenReGenerator, jwtHttpCookieInjector),
				UsernamePasswordAuthenticationFilter::class.java,
			)
}