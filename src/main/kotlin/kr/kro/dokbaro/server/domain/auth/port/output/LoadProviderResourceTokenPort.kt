package kr.kro.dokbaro.server.domain.auth.port.output

interface LoadProviderResourceTokenPort {
	fun getToken(authorizationToken: String): String
}