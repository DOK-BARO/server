package kr.kro.dokbaro.server.core.auth.oauth2.domain

import java.util.UUID

data class OAuth2CertificatedAccount(
	val certificationId: UUID,
	val role: Set<String>,
)