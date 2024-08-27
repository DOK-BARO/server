package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount

fun interface LoadOAuth2ProviderAccountPort {
	fun getProviderAccount(
		provider: AuthProvider,
		accessToken: String,
	): OAuth2ProviderAccount
}