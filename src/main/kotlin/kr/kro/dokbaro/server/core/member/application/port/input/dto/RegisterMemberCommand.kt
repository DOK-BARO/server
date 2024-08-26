package kr.kro.dokbaro.server.core.member.application.port.input.dto

data class RegisterMemberCommand(
	val nickName: String,
	val email: String,
	val profileImage: String?,
)