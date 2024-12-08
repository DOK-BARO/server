package kr.kro.dokbaro.server.core.account.application.port.input.dto

data class RegisterEmailAccountCommand(
	val email: String,
	val nickname: String,
	val password: String,
	val profileImage: String?,
)