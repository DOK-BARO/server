package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import java.time.LocalDateTime
import java.util.UUID

fun refreshTokenFixture(
	token: String = UUID.randomUUID().toString(),
	certificateId: UUID = UUID.randomUUID(),
	expiredAt: LocalDateTime = LocalDateTime.now().plusWeeks(2),
	used: Boolean = false,
): RefreshToken =
	RefreshToken(
		token,
		certificateId,
		expiredAt,
		used,
	)