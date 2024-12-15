package kr.kro.dokbaro.server.security.jwt.cookie

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class JwtHttpCookieRemover(
	@Value("\${spring.security.server-domain}") private val domain: String,
) {
	fun remove(response: HttpServletResponse) {
		response.addHeader(
			HttpHeaders.SET_COOKIE,
			toCookie(
				name = SecurityConstants.AUTHORIZATION,
			),
		)
		response.addHeader(
			HttpHeaders.SET_COOKIE,
			toCookie(
				name = SecurityConstants.REFRESH,
			),
		)
	}

	private fun toCookie(
		name: String,
		path: String = "/",
	): String =
		ResponseCookie
			.from(name, "")
			.sameSite("None")
			.domain(domain)
			.maxAge(0)
			.path(path)
			.secure(true)
			.httpOnly(true)
			.build()
			.toString()
}