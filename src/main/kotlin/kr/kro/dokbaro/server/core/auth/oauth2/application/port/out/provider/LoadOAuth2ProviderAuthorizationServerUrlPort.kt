package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider

import kr.kro.dokbaro.server.common.type.AuthProvider

fun interface LoadOAuth2ProviderAuthorizationServerUrlPort {
	fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String
}