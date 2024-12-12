package kr.kro.dokbaro.server.core.account.application.port.input.dto

data class ChangePasswordCommand(
	val memberId: Long,
	val oldPassword: String,
	val newPassword: String,
)