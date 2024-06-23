package kr.kro.dokbaro.server.domain.account.port.input.command.dto

data class RegisterAccountCommand(
	val socialAccountId: String,
	val provider: String,
)