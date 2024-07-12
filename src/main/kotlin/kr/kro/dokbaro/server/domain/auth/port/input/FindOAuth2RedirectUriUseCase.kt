package kr.kro.dokbaro.server.domain.auth.port.input

interface FindOAuth2RedirectUriUseCase {
	fun getRedirectUri(provider: String): String
}