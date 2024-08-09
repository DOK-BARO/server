package kr.kro.dokbaro.server.core.auth.application.port.out

import kr.kro.dokbaro.server.common.type.AuthProvider

fun interface LoadProviderResourceTokenPort {
	fun getToken(
		provider: AuthProvider,
		authorizationToken: String,
		redirectUrl: String,
	): String
}