package kr.kro.dokbaro.server.domain.token.model

data class AuthToken(
	val accessToken: String,
	val refreshToken: String,
)