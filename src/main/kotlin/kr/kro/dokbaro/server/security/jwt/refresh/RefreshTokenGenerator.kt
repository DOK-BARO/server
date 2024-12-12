package kr.kro.dokbaro.server.security.jwt.refresh

import kr.kro.dokbaro.server.common.util.UUIDUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

@Component
class RefreshTokenGenerator(
	private val clock: Clock,
	private val refreshTokenRepository: RefreshTokenRepository,
	@Value("\${jwt.limit-refresh-days}")private val limitRefreshDays: Long,
) {
	fun generate(certificationId: UUID): RefreshToken {
		val refreshToken =
			RefreshToken(
				tokenValue = UUIDUtils.uuidToString(UUID.randomUUID()),
				certificationId = certificationId,
				used = false,
				expiredAt = LocalDateTime.now(clock).plusDays(limitRefreshDays),
			)

		refreshTokenRepository.insert(refreshToken)

		return refreshToken
	}
}