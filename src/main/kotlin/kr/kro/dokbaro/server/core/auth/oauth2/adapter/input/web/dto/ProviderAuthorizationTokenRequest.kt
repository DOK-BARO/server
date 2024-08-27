package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web.dto

data class ProviderAuthorizationTokenRequest(
	val token: String,
	val redirectUrl: String,
)