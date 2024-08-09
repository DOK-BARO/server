package kr.kro.dokbaro.server.core.token.domain.refresh

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RefreshTokenGeneratorTest :
	StringSpec({
		val refreshTokenGenerator = RefreshTokenGenerator()
		"refresh token을 생성한다" {
			refreshTokenGenerator.generate("account").isNotEmpty() shouldBe true
		}
	})