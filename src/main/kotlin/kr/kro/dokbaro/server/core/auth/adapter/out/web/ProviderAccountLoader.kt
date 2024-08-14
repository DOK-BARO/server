package kr.kro.dokbaro.server.core.auth.adapter.out.web

import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount

fun interface ProviderAccountLoader {
	fun get(accessToken: String): ProviderAccount
}