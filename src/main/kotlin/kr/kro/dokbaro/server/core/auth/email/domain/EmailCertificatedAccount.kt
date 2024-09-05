package kr.kro.dokbaro.server.core.auth.email.domain

import java.util.UUID

data class EmailCertificatedAccount(
	val password: String,
	val certificationId: UUID,
	val role: Set<String>,
)