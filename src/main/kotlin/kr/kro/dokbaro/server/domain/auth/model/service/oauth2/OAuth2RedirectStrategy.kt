package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

interface OAuth2RedirectStrategy {
	fun getUri(): String
}