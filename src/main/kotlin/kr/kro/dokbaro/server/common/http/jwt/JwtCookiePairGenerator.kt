package kr.kro.dokbaro.server.common.http.jwt

import kr.kro.dokbaro.server.common.http.CookieGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class JwtCookiePairGenerator(
	private val cookieGenerator: CookieGenerator,
	@Value("\${jwt.access-header-name}") private val accessHeaderName: String,
	@Value("\${jwt.refresh-header-name}") private val refreshHeaderName: String,
	@Value("\${jwt.limit-minute}") private val accessTokenLimitMinute: Long,
	@Value("\${jwt.limit-refresh-days}") private val refreshLimitDays: Long,
) {
	fun getJwtCookiePair(
		accessToken: String,
		refreshToken: String,
	): JwtCookiePair =
		JwtCookiePair(
			accessTokenCookie =
				cookieGenerator.generate(
					accessHeaderName,
					accessToken,
					Duration.ofMinutes(accessTokenLimitMinute),
				),
			refreshTokenCookie =
				cookieGenerator.generate(
					refreshHeaderName,
					refreshToken,
					Duration.ofDays(refreshLimitDays),
				),
		)
}