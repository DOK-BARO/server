package kr.kro.dokbaro.server.core.token.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.token.application.port.out.DeleteRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.InsertRefreshTokenPort
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import kr.kro.dokbaro.server.fixture.domain.refreshTokenFixture
import java.util.UUID

class GenerateAuthTokenServiceTest :
	StringSpec({
		val accessTokenGenerator = mockk<AccessTokenGenerator>()
		val insertRefreshTokenPort = mockk<InsertRefreshTokenPort>()
		val refreshTokenGenerator = mockk<RefreshTokenGenerator>()
		val deleteRefreshTokenPort = mockk<DeleteRefreshTokenPort>()

		val generateAuthTokenService =
			GenerateAuthTokenService(
				accessTokenGenerator,
				insertRefreshTokenPort,
				refreshTokenGenerator,
				deleteRefreshTokenPort,
			)

		"token을 생성한다" {
			every { accessTokenGenerator.generate(any()) } returns "access-token"
			every { refreshTokenGenerator.generate(any()) } returns refreshTokenFixture()
			every { deleteRefreshTokenPort.deleteBy(any()) } returns Unit
			every { insertRefreshTokenPort.insert(any()) } returns Unit

			val tokenClaims = TokenClaims(UUID.randomUUID().toString(), setOf("USER"))

			val result: AuthToken = generateAuthTokenService.generate(tokenClaims)

			result.accessToken.isNotEmpty() shouldBe true
			result.refreshToken.isNotEmpty() shouldBe true
		}
	})