package kr.kro.dokbaro.server.core.auth.application.port.out

import kr.kro.dokbaro.server.common.type.AuthProvider

interface LoadProviderAuthorizationServerUrlPort {
	fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String
}