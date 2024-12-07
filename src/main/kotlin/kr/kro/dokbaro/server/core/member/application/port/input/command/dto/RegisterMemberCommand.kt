package kr.kro.dokbaro.server.core.member.application.port.input.command.dto

data class RegisterMemberCommand(
	val nickname: String,
	val email: String,
	val profileImage: String?,
)