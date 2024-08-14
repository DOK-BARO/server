package kr.kro.dokbaro.server.core.auth.adapter.out.web

fun interface ProviderResourceTokenLoader {
	fun get(
		authorizationToken: String,
		redirectUrl: String,
	): String
}