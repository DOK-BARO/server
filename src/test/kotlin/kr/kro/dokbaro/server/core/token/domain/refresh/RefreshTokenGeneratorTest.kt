package kr.kro.dokbaro.server.core.token.domain.refresh

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.core.token.application.service.RefreshTokenGenerator
import java.time.Clock
import java.util.UUID

class RefreshTokenGeneratorTest :
	StringSpec({
		val refreshTokenGenerator = RefreshTokenGenerator(14, Clock.systemUTC())
		"refresh token을 생성한다" {
			refreshTokenGenerator.generate(UUID.randomUUID()) shouldNotBe null
		}
	})