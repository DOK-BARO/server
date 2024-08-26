package kr.kro.dokbaro.server.core.auth.oauth2.domain

import kr.kro.dokbaro.server.common.type.AuthProvider

data class OAuth2Account(
	val socialId: String,
	val provider: AuthProvider,
	val memberId: Long,
	val id: Long = UNSAVED_ACCOUNT_ID,
) {
	companion object {
		private const val UNSAVED_ACCOUNT_ID = 0L
	}
}