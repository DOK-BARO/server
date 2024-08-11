package kr.kro.dokbaro.server.core.auth.adapter.out.properties

interface OAuth2AuthorizeServerUrlLoader {
	fun getUrl(redirectUrl: String): String
}