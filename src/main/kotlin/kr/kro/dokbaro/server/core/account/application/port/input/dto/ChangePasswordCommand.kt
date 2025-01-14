package kr.kro.dokbaro.server.core.account.application.port.input.dto

/**
 * 비밀번호 변경에 필요한 Command DTO 입니다
 */
data class ChangePasswordCommand(
	val memberId: Long,
	val oldPassword: String,
	val newPassword: String,
)