package kr.kro.dokbaro.server.core.account.application.port.input.dto

import kr.kro.dokbaro.server.core.account.domain.AuthProvider

data class RegisterSocialAccountCommand(
	val socialId: String,
	val email: String,
	val nickname: String,
	val provider: AuthProvider,
	val profileImage: String?,
)