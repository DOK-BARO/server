package kr.kro.dokbaro.server.domain.auth.adapter.output.google.external

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleAuthorizationTokenResponse(
	val tokenType: String,
	val accessToken: String,
	val expiresIn: Int,
	val refreshToken: String,
	val scope: String?,
)