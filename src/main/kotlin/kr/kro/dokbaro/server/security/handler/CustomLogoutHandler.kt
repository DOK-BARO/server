package kr.kro.dokbaro.server.security.handler

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

@Component
class CustomLogoutHandler : LogoutHandler {
	override fun logout(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication,
	) {
		response.status

		val accessCookie = Cookie(SecurityConstants.AUTHORIZATION, null)
		accessCookie.maxAge = 0
		response.addCookie(accessCookie)

		val refreshCookie = Cookie(SecurityConstants.REFRESH, null)
		refreshCookie.maxAge = 0
		response.addCookie(refreshCookie)
	}
}