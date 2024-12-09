package kr.kro.dokbaro.server.security.jwt.refresh

import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

data class RefreshToken(
	val tokenValue: String,
	val certificationId: UUID,
	var used: Boolean = false,
	val expiredAt: LocalDateTime,
) {
	fun isExpired(clock: Clock): Boolean = expiredAt.isBefore(LocalDateTime.now(clock))

	fun use() {
		if (used) {
			throw AlreadyUsedRefreshTokenException()
		}
		used = true
	}
}