package kr.kro.dokbaro.server.core.auth.application.port.input

import kr.kro.dokbaro.server.global.AuthProvider

fun interface FindOAuth2AuthorizeUrlUseCase {
	fun get(provider: AuthProvider): String
}