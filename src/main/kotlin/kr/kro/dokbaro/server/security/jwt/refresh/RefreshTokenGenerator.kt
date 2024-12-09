package kr.kro.dokbaro.server.security.jwt.refresh

import kr.kro.dokbaro.server.common.util.UUIDUtils
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

@Component
class RefreshTokenGenerator(
	private val clock: Clock,
) {
	fun generate(certificationId: UUID): RefreshToken =
		RefreshToken(
			tokenValue = UUIDUtils.uuidToString(UUID.randomUUID()),
			certificationId = certificationId,
			used = false,
			expiredAt = LocalDateTime.now(clock),
		)
}