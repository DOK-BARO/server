package kr.kro.dokbaro.server.domain.token

data class ClaimsAttribute(
	val name: String,
	val type: Class<*>,
)