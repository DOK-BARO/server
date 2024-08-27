package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class JwtResponseGenerator(
	private val cookieGenerator: CookieGenerator,
	@Value("\${jwt.access-header-name}") private val accessHeaderName: String,
	@Value("\${jwt.refresh-header-name}") private val refreshHeaderName: String,
	@Value("\${jwt.limit-minute}") private val jwtLimitMinute: Long,
) {
	fun getResponseBuilder(
		accessToken: String,
		refreshToken: String,
	): ResponseEntity.BodyBuilder =
		ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, cookieGenerator.generate(accessHeaderName, accessToken, jwtLimitMinute))
			.header(HttpHeaders.SET_COOKIE, cookieGenerator.generate(refreshHeaderName, refreshToken, jwtLimitMinute))
}