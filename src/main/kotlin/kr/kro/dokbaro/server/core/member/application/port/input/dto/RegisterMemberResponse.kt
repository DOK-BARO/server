package kr.kro.dokbaro.server.core.member.application.port.input.dto

import java.util.UUID

data class RegisterMemberResponse(
	val certificationId: UUID,
)