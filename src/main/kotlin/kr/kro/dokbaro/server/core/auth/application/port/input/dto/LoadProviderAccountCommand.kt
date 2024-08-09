package kr.kro.dokbaro.server.core.auth.application.port.input.dto

import kr.kro.dokbaro.server.common.type.AuthProvider

data class LoadProviderAccountCommand(
	val provider: AuthProvider,
	val token: String,
	val redirectUrl: String,
)