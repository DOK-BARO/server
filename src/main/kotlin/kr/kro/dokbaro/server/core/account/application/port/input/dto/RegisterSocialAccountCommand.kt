package kr.kro.dokbaro.server.core.account.application.port.input.dto

import kr.kro.dokbaro.server.core.account.domain.AuthProvider

/**
 * Social 계정 등록에 필요한 Command DTO 입니다.
 */
data class RegisterSocialAccountCommand(
	val socialId: String,
	val email: String?,
	val nickname: String,
	val provider: AuthProvider,
	val profileImage: String?,
)