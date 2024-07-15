package kr.kro.dokbaro.server.domain.auth.port.output

fun interface LoadProviderResourceTokenPort {
	fun getToken(authorizationToken: String): String
}