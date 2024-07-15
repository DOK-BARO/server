package kr.kro.dokbaro.server.domain.account.port.input.command.dto

import kr.kro.dokbaro.server.global.AuthProvider

data class RegisterAccountCommand(
	val socialId: String,
	val provider: AuthProvider,
)