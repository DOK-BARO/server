package kr.kro.dokbaro.server.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class FormAuthenticationFailureHandler : AuthenticationFailureHandler {
	override fun onAuthenticationFailure(
		request: HttpServletRequest,
		response: HttpServletResponse,
		exception: AuthenticationException,
	) {
		response.status = HttpServletResponse.SC_UNAUTHORIZED
	}
}