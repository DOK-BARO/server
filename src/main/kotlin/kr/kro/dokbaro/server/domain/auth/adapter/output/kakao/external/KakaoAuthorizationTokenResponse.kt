package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KakaoAuthorizationTokenResponse(
	val tokenType: String,
	val accessToken: String,
	val idToken: String?,
	val expiresIn: Int,
	val refreshToken: String,
	val refreshTokenExpiresIn: Int,
	val scope: String?,
)