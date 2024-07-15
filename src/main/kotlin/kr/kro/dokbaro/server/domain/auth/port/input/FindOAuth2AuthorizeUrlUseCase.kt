package kr.kro.dokbaro.server.domain.auth.port.input

import kr.kro.dokbaro.server.global.AuthProvider

fun interface FindOAuth2AuthorizeUrlUseCase {
	fun get(provider: AuthProvider): String
}