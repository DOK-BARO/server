package kr.kro.dokbaro.server.domain.account.model

import java.time.LocalDateTime

class Account(
	val id: Long,
	val socialId: String,
	val provider: Provider,
	val roles: Set<Role>,
	val registeredAt: LocalDateTime,
)