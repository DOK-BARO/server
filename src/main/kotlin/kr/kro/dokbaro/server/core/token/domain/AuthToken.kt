package kr.kro.dokbaro.server.core.token.domain

data class AuthToken(
	val accessToken: String,
	val refreshToken: String,
)