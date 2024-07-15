package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize

fun interface OAuth2AuthorizeStrategy {
	fun getUri(): String
}