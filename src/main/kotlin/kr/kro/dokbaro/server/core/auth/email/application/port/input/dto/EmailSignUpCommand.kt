package kr.kro.dokbaro.server.core.auth.email.application.port.input.dto

data class EmailSignUpCommand(
	val email: String,
	val password: String,
	val nickname: String,
	val profileImage: String?,
)