package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.properties

fun interface OAuth2AuthorizeServerUrlLoader {
	fun getUrl(redirectUrl: String): String
}