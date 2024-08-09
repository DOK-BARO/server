package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.type.AuthProvider
import java.time.Clock
import java.time.LocalDateTime

class Account(
	val id: Long,
	val socialId: String,
	val provider: AuthProvider,
	val roles: Set<Role>,
	val registeredAt: LocalDateTime,
) {
	companion object {
		private const val UNSAVED_ACCOUNT_ID = 0L

		fun init(
			socialId: String,
			provider: AuthProvider,
			clock: Clock,
		): Account =
			Account(
				UNSAVED_ACCOUNT_ID,
				socialId,
				provider,
				setOf(Role.GUEST),
				LocalDateTime.now(clock),
			)
	}
}