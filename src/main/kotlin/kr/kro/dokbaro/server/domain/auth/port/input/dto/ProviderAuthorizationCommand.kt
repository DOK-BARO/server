package kr.kro.dokbaro.server.domain.auth.port.input.dto

import kr.kro.dokbaro.server.global.AuthProvider

data class ProviderAuthorizationCommand(
	val provider: AuthProvider,
	val token: String,
)