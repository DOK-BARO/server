package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount

fun interface LoadOAuth2CertificatedAccountPort {
	fun findBy(
		socialId: String,
		provider: AuthProvider,
	): OAuth2CertificatedAccount?
}