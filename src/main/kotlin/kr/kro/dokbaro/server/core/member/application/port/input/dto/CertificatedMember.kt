package kr.kro.dokbaro.server.core.member.application.port.input.dto

import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import java.util.UUID

data class CertificatedMember(
	var nickName: String,
	var email: String,
	var profileImage: String?,
	val certificationId: UUID,
	val roles: Set<Role>,
	val id: Long,
) {
	constructor(member: Member) : this(
		member.nickName,
		member.email.address,
		member.profileImage,
		member.certificationId,
		member.roles,
		member.id,
	)
}