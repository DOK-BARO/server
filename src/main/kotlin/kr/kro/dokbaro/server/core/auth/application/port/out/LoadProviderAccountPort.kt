package kr.kro.dokbaro.server.core.auth.application.port.out

import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount

fun interface LoadProviderAccountPort {
	fun getAttributes(accessToken: String): ProviderAccount
}