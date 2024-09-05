package kr.kro.dokbaro.server.core.auth.email.domain

data class AccountPassword(
	val password: String,
	val memberId: Long,
	val id: Long = UNSAVED_ACCOUNT_PASSWORD_ID,
) {
	companion object {
		private const val UNSAVED_ACCOUNT_PASSWORD_ID = 0L
	}
}