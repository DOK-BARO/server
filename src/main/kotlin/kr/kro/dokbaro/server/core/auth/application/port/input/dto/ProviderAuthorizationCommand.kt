package kr.kro.dokbaro.server.core.auth.application.port.input.dto

import kr.kro.dokbaro.server.global.AuthProvider

data class ProviderAuthorizationCommand(
	val provider: AuthProvider,
	val token: String,
	val redirectUrl: String,
)