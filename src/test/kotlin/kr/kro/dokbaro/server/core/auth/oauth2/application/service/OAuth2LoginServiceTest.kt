package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.LoadOAuth2CertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception.NotFoundAccountException
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class OAuth2LoginServiceTest :
	StringSpec({
		val providerAccountLoader = mockk<OAuth2ProviderAccountLoader>()
		val loadOAuth2CertificatedAccountPort = mockk<LoadOAuth2CertificatedAccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2LoginService =
			OAuth2LoginService(providerAccountLoader, loadOAuth2CertificatedAccountPort, generateAuthTokenUseCase)

		"login을 수행한다" {
			every { providerAccountLoader.getAccount(any()) } returns FixtureBuilder.give<OAuth2ProviderAccount>().sample()
			every { loadOAuth2CertificatedAccountPort.findBy(any(), any()) } returns
				FixtureBuilder.give<OAuth2CertificatedAccount>().sample()
			every { generateAuthTokenUseCase.generate(any()) } returns AuthToken("aaa", "rrr")

			val command =
				LoadProviderAccountCommand(
					AuthProvider.GOOGLE,
					"token",
					"http://localhost:5173/oauth2/redirected/kakao",
				)

			val result = oAuth2LoginService.login(command)

			result.accessToken.isNotBlank() shouldBe true
			result.refreshToken.isNotBlank() shouldBe true
		}

		"만약 존재하는 인증 계정이 없으면 예외를 반환한다" {

			every { providerAccountLoader.getAccount(any()) } returns FixtureBuilder.give<OAuth2ProviderAccount>().sample()
			every { loadOAuth2CertificatedAccountPort.findBy(any(), any()) } returns null
			every { generateAuthTokenUseCase.generate(any()) } returns AuthToken("aaa", "rrr")

			val command =
				LoadProviderAccountCommand(
					AuthProvider.GOOGLE,
					"token",
					"http://localhost:5173/oauth2/redirected/kakao",
				)

			shouldThrow<NotFoundAccountException> {
				oAuth2LoginService.login(command)
			}
		}
	})