package kr.kro.dokbaro.server.core.member.application.port.input.dto

import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import java.util.UUID

data class CertificatedMember(
	val id: Long,
	var nickName: String,
	var email: String,
	var profileImage: String?,
	val certificationId: UUID,
	val roles: Set<Role>,
) {
	constructor(member: Member) : this(
		id = member.id,
		nickName = member.nickName,
		email = member.email.address,
		profileImage = member.profileImage,
		certificationId = member.certificationId,
		roles = member.roles,
	)
}