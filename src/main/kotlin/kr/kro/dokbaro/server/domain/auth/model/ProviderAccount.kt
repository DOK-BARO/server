package kr.kro.dokbaro.server.domain.auth.model

data class ProviderAccount(
	val provider: String,
	val id: String,
	val name: String?,
	val email: String?,
	val profileImage: String?,
)