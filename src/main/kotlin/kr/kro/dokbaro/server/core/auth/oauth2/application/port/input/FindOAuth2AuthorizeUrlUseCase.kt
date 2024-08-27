package kr.kro.dokbaro.server.core.auth.oauth2.application.port.input

import kr.kro.dokbaro.server.common.type.AuthProvider

fun interface FindOAuth2AuthorizeUrlUseCase {
	fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String
}