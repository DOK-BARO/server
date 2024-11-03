package kr.kro.dokbaro.server.core.token.domain.refresh

import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

class RefreshToken(
	val token: String,
	val certificateId: UUID,
	val expiredAt: LocalDateTime,
	var used: Boolean = false,
) {
	fun isExpired(clock: Clock): Boolean = expiredAt.isBefore(LocalDateTime.now(clock))

	fun use() {
		used = true
	}
}