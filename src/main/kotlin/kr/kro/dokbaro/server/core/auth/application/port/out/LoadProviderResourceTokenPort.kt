package kr.kro.dokbaro.server.core.auth.application.port.out

fun interface LoadProviderResourceTokenPort {
	fun getToken(
		authorizationToken: String,
		redirectUrl: String,
	): String
}