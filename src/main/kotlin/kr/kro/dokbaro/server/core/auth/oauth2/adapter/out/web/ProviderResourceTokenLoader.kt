package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web

fun interface ProviderResourceTokenLoader {
	fun getResource(
		authorizationToken: String,
		redirectUrl: String,
	): String
}