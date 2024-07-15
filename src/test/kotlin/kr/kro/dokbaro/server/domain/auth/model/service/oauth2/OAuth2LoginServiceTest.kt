package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.input.query.AccountFinder
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.fixture.FixtureBuilder
import kr.kro.dokbaro.server.global.AuthProvider

class OAuth2LoginServiceTest :
	StringSpec({
		val providerAccountLoader = mockk<ProviderAccountLoader>()
		val accountFinder = mockk<AccountFinder>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2LoginService = OAuth2LoginService(providerAccountLoader, accountFinder, generateAuthTokenUseCase)

		"login을 수행한다" {
			every { providerAccountLoader.load(any()) } returns FixtureBuilder.give<ProviderAccount>().sample()
			every { accountFinder.getById(any()) } returns
				AccountResponse(FixtureBuilder.give<Account>().sample())
			every { generateAuthTokenUseCase.generate(any()) } returns FixtureBuilder.give<AuthToken>().sample()

			val command = ProviderAuthorizationCommand(AuthProvider.GOOGLE, "token")

			val result = oAuth2LoginService.login(command)

			result.accessToken.isNotBlank() shouldBe true
			result.refreshToken.isNotBlank() shouldBe true
		}
	})