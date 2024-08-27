package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider

import kr.kro.dokbaro.server.common.type.AuthProvider

fun interface LoadOAuth2ProviderResourceTokenPort {
	fun getToken(
		provider: AuthProvider,
		authorizationToken: String,
		redirectUrl: String,
	): String
}