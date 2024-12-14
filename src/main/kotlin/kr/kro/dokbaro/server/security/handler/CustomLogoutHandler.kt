package kr.kro.dokbaro.server.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.jwt.cookie.JwtHttpCookieRemover
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

@Component
class CustomLogoutHandler(
	private val cookieRemover: JwtHttpCookieRemover,
) : LogoutHandler {
	override fun logout(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication?,
	) {
		cookieRemover.remove(response)
	}
}