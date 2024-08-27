package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web

import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount

fun interface ProviderAccountLoader {
	fun get(accessToken: String): OAuth2ProviderAccount
}