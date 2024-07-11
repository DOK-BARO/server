package kr.kro.dokbaro.server.domain.auth.port.input.dto

data class ProviderAuthorizationCommand(
	val provider: String,
	val token: String,
)