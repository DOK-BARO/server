package kr.kro.dokbaro.server.core.token.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenDecoder
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims

class DecodeAccessTokenServiceTest :
	StringSpec({
		val accessTokenDecoder = mockk<AccessTokenDecoder>()
		val decodeAccessTokenService = DecodeAccessTokenService(accessTokenDecoder)

		"decode를 수행한다" {
			val expect = TokenClaims("id", setOf("ADMIN"))
			every { accessTokenDecoder.decode(any()) } returns expect

			decodeAccessTokenService.decode("accessToken") shouldBe expect
		}
	})