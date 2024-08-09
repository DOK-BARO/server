package kr.kro.dokbaro.server.core.auth.adapter.out.web

interface ProviderResourceTokenLoader {
	fun get(
		authorizationToken: String,
		redirectUrl: String,
	): String
}