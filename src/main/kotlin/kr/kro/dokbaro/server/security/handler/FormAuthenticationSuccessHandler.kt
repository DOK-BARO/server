package kr.kro.dokbaro.server.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.jwt.JwtHttpCookieInjector
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class FormAuthenticationSuccessHandler(
	private val jwtTokenGenerator: JwtTokenGenerator,
	private val jwtHttpCookieInjector: JwtHttpCookieInjector,
) : AuthenticationSuccessHandler {
	override fun onAuthenticationSuccess(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication,
	) {
		response.status = HttpServletResponse.SC_OK

		val jwtToken: JwtResponse = jwtTokenGenerator.generate(UUID.fromString(authentication.principal.toString()))

		jwtHttpCookieInjector.inject(response, jwtToken)
	}
}