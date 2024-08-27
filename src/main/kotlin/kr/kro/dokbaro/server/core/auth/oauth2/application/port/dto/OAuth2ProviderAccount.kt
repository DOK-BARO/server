package kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto

import kr.kro.dokbaro.server.common.type.AuthProvider

data class OAuth2ProviderAccount(
	val provider: AuthProvider,
	val id: String,
	val name: String,
	val email: String,
	val profileImage: String?,
)