package kr.kro.dokbaro.server.domain.auth.port.output

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.ProviderAccount

interface LoadProviderAccountPort {
	fun getAttributes(accessToken: String): ProviderAccount
}