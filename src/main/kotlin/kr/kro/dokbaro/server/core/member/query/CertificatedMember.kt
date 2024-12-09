package kr.kro.dokbaro.server.core.member.query

import java.util.UUID

data class CertificatedMember(
	val id: Long,
	val certificationId: UUID,
	val nickname: String,
	val email: String,
	val role: Collection<String>,
)