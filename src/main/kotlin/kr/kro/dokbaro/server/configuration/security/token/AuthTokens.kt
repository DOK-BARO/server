package kr.kro.dokbaro.server.configuration.security.token

data class AuthTokens(
	val accessToken: String,
	val refreshToken: String,
)