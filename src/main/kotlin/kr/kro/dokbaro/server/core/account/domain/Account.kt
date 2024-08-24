package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.type.AuthProvider

data class Account(
	val socialId: String,
	val provider: AuthProvider,
	val memberId: String,
	val id: Long = UNSAVED_ACCOUNT_ID,
) {
	companion object {
		private const val UNSAVED_ACCOUNT_ID = 0L
	}
}