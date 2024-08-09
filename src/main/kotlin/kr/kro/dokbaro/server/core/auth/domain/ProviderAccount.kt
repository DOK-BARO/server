package kr.kro.dokbaro.server.core.auth.domain

import kr.kro.dokbaro.server.common.type.AuthProvider

data class ProviderAccount(
	val provider: AuthProvider,
	val id: String,
	val name: String?,
	val email: String?,
	val profileImage: String?,
)