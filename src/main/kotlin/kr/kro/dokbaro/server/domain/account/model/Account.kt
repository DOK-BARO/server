package kr.kro.dokbaro.server.domain.account.model

import kr.kro.dokbaro.server.global.AuthProvider
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