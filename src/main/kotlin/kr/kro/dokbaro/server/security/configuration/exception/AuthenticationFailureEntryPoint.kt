package kr.kro.dokbaro.server.security.configuration.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.jwt.cookie.JwtHttpCookieRemover
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureEntryPoint(
	private val cookieRemover: JwtHttpCookieRemover,
) : AuthenticationEntryPoint {
	override fun commence(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authException: AuthenticationException,
	) {
		response.status = HttpServletResponse.SC_UNAUTHORIZED

		cookieRemover.remove(response)
	}
}