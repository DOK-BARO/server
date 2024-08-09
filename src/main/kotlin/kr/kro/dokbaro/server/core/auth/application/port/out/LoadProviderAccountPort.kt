package kr.kro.dokbaro.server.core.auth.application.port.out

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount

fun interface LoadProviderAccountPort {
	fun getProviderAccount(
		provider: AuthProvider,
		accessToken: String,
	): ProviderAccount
}