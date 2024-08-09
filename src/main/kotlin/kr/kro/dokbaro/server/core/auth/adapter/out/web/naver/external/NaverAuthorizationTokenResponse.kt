package kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NaverAuthorizationTokenResponse(
	val accessToken: String,
	val refreshToken: String,
	val tokenType: String,
	val expiresIn: Long,
)