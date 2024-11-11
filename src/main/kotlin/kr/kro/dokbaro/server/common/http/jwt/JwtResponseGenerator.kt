package kr.kro.dokbaro.server.common.http.jwt

import kr.kro.dokbaro.server.common.http.CookieGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class JwtResponseGenerator(
	private val cookieGenerator: CookieGenerator,
	@Value("\${jwt.access-header-name}") private val accessHeaderName: String,
	@Value("\${jwt.refresh-header-name}") private val refreshHeaderName: String,
	@Value("\${jwt.limit-minute}") private val accessTokenLimitMinute: Long,
	@Value("\${jwt.limit-refresh-days}") private val refreshLimitDays: Long,
) {
	fun getResponseBuilder(
		accessToken: String,
		refreshToken: String,
	): ResponseEntity.BodyBuilder =
		ResponseEntity
			.ok()
			.header(
				HttpHeaders.SET_COOKIE,
				cookieGenerator.generate(accessHeaderName, accessToken, Duration.ofMinutes(accessTokenLimitMinute)),
			).header(
				HttpHeaders.SET_COOKIE,
				cookieGenerator.generate(refreshHeaderName, refreshToken, Duration.ofDays(refreshLimitDays)),
			)
}