package kr.kro.dokbaro.server.security.jwt.refresh

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class RefreshTokenTest :
	StringSpec({

		val clock = Clock.fixed(Instant.now(), ZoneId.of("UTC"))

		"토큰 만료를 확인한다" {

			val refreshToken =
				RefreshToken(
					"",
					UUID.randomUUID(),
					false,
					expiredAt = LocalDateTime.now(clock).plusDays(15),
				)

			refreshToken.isExpired(clock) shouldBe false
		}
	})