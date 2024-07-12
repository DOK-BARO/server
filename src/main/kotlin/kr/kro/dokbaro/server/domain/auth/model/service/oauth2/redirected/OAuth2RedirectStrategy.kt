package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.redirected

interface OAuth2RedirectStrategy {
	fun getUri(): String
}