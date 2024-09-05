package kr.kro.dokbaro.server.core.auth.email.application.port.input.dto

data class EmailLoginCommand(
	val email: String,
	val password: String,
)