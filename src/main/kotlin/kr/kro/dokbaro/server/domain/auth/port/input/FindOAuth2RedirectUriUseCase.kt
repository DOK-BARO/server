package kr.kro.dokbaro.server.domain.auth.port.input

import kr.kro.dokbaro.server.global.AuthProvider

interface FindOAuth2RedirectUriUseCase {
	fun getRedirectUri(provider: AuthProvider): String
}