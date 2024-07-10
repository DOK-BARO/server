package kr.kro.dokbaro.server.domain.token

data class AuthTokens(
	val accessToken: String,
	val refreshToken: String,
)