package kr.kro.dokbaro.server.domain.token.model.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.model.refresh.RefreshTokenGenerator

class GenerateAuthTokenServiceTest :
	StringSpec({
		val accessTokenGenerator = mockk<AccessTokenGenerator>()
		val refreshTokenGenerator = mockk<RefreshTokenGenerator>()

		val generateAuthTokenService = GenerateAuthTokenService(accessTokenGenerator, refreshTokenGenerator)

		"token을 생성한다" {
			every { accessTokenGenerator.generate(any()) } returns "access-token"
			every { refreshTokenGenerator.generate(any()) } returns "access-token"
			val tokenClaims = TokenClaims("id", setOf("USER"))

			val result: AuthToken = generateAuthTokenService.generate(tokenClaims)

			result.accessToken.isNotEmpty() shouldBe true
			result.refreshToken.isNotEmpty() shouldBe true
		}
	})