package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.account.application.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.fixture.FixtureBuilder
import kr.kro.dokbaro.server.global.AuthProvider

class OAuth2SignUpServiceTest :
	StringSpec({
		val providerAccountLoader = mockk<ProviderAccountLoader>()
		val registerAccountUseCase = mockk<RegisterAccountUseCase>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2SignUpService =
			OAuth2SignUpService(providerAccountLoader, registerAccountUseCase, generateAuthTokenUseCase)

		"회원가입을 수행한다" {
			every { providerAccountLoader.load(any()) } returns FixtureBuilder.give<ProviderAccount>().sample()
			every { registerAccountUseCase.register(any()) } returns
				AccountResponse(FixtureBuilder.give<Account>().sample())
			every { generateAuthTokenUseCase.generate(any()) } returns FixtureBuilder.give<AuthToken>().sample()

			val command =
				ProviderAuthorizationCommand(AuthProvider.GOOGLE, "token", "http://localhost:5173/oauth2/redirected/kakao")
			val result = oAuth2SignUpService.signUp(command)

			result.accessToken.isNotBlank() shouldBe true
			result.refreshToken.isNotBlank() shouldBe true
		}
	})