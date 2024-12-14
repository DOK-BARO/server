package kr.kro.dokbaro.server.security.jwt.cookie

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class JwtHttpCookieInjector(
	@Value("\${spring.security.server-domain}") private val domain: String,
	@Value("\${jwt.limit-minute}") private val accessTokenAgeMinute: Long,
	@Value("\${jwt.limit-refresh-days}") private val refreshTokenAgeDays: Long,
) {
	fun inject(
		response: HttpServletResponse,
		jwt: JwtResponse,
	) {
		response.addHeader(
			HttpHeaders.SET_COOKIE,
			toCookie(
				name = SecurityConstants.AUTHORIZATION,
				value = jwt.accessToken,
				age = Duration.ofMinutes(accessTokenAgeMinute - 1),
			),
		)
		response.addHeader(
			HttpHeaders.SET_COOKIE,
			toCookie(
				name = SecurityConstants.REFRESH,
				value = jwt.refreshToken,
				age = Duration.ofDays(refreshTokenAgeDays - 1),
			),
		)
	}

	private fun toCookie(
		name: String,
		value: String,
		age: Duration,
		path: String = "/",
	): String =
		ResponseCookie
			.from(name, value)
			.sameSite("None")
			.domain(domain)
			.maxAge(age)
			.path(path)
			.secure(true)
			.httpOnly(true)
			.build()
			.toString()
}