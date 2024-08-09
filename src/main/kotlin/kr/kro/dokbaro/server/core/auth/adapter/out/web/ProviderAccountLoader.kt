package kr.kro.dokbaro.server.core.auth.adapter.out.web

import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount

interface ProviderAccountLoader {
	fun get(accessToken: String): ProviderAccount
}