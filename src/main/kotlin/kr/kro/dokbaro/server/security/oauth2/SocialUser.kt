package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider

data class SocialUser(
	val id: String,
	val email: String?,
	val nickname: String,
	val provider: AuthProvider,
	val profileImage: String?,
)