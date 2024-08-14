package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.type.AuthProvider
import java.time.Clock
import java.time.LocalDateTime

data class Account(
	val socialId: String,
	val provider: AuthProvider,
	val roles: Set<Role>,
	val registeredAt: LocalDateTime,
	val id: Long = UNSAVED_ACCOUNT_ID,
) {
	companion object {
		private const val UNSAVED_ACCOUNT_ID = 0L

		fun init(
			socialId: String,
			provider: AuthProvider,
			clock: Clock,
			id: Long = UNSAVED_ACCOUNT_ID,
		): Account =
			Account(
				socialId,
				provider,
				setOf(Role.GUEST),
				LocalDateTime.now(clock),
				id,
			)
	}
}