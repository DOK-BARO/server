package kr.kro.dokbaro.server.security.jwt

data class JwtResponse(
	val accessToken: String,
	val refreshToken: String,
)