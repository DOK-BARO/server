package kr.kro.dokbaro.server.common.http.jwt

data class JwtCookiePair(
	val accessTokenCookie: String,
	val refreshTokenCookie: String,
)