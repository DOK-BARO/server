package kr.kro.dokbaro.server.configuration.security.token

data class TokenClaims(
	val id: String,
	val role: Collection<String>,
)