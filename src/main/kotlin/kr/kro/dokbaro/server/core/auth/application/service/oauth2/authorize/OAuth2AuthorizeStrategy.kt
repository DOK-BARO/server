package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

fun interface OAuth2AuthorizeStrategy {
	fun getUri(): String
}