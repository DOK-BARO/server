package kr.kro.dokbaro.server.core.member.application.port.input.command.dto

import java.util.UUID

data class ModifyMemberCommand(
	val certificationId: UUID,
	val nickname: String?,
	val email: String?,
	val profileImage: String?,
)