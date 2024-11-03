package kr.kro.dokbaro.server.core.token.application.service

import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

@Component
class RefreshTokenGenerator(
	@Value("\${jwt.limit-refresh-days}") private val limitRefreshDays: Long,
	private val clock: Clock,
) {
	fun generate(certificateId: UUID): RefreshToken =
		RefreshToken(
			UUID.randomUUID().toString(),
			certificateId,
			LocalDateTime.now(clock).plusDays(limitRefreshDays),
		)
}