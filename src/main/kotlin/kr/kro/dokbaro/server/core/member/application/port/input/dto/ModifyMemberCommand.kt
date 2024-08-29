package kr.kro.dokbaro.server.core.member.application.port.input.dto

import java.util.UUID

data class ModifyMemberCommand(
	val certificationId: UUID,
	val nickName: String?,
	val email: String?,
	val profileImage: String?,
)