package kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto

import kr.kro.dokbaro.server.common.type.AuthProvider

data class LoadProviderAccountCommand(
	val provider: AuthProvider,
	val token: String,
	val redirectUrl: String,
)