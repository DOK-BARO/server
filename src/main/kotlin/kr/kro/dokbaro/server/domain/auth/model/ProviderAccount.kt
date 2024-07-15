package kr.kro.dokbaro.server.domain.auth.model

import kr.kro.dokbaro.server.global.AuthProvider

data class ProviderAccount(
	val provider: AuthProvider,
	val id: String,
	val name: String?,
	val email: String?,
	val profileImage: String?,
)