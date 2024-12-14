package kr.kro.dokbaro.server.security.configuration.exception

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureEntryPoint : AuthenticationEntryPoint {
	override fun commence(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authException: AuthenticationException,
	) {
		response.status = HttpServletResponse.SC_UNAUTHORIZED

		val accessCookie = Cookie(SecurityConstants.AUTHORIZATION, null)
		accessCookie.maxAge = 0
		response.addCookie(accessCookie)

		val refreshCookie = Cookie(SecurityConstants.REFRESH, null)
		refreshCookie.maxAge = 0
		response.addCookie(refreshCookie)
	}
}