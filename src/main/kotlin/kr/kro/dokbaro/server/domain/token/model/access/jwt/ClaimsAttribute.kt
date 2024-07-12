package kr.kro.dokbaro.server.domain.token.model.access.jwt

data class ClaimsAttribute(
	val name: String,
	val type: Class<*>,
)