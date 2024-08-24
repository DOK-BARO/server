package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.input.query.FindCertificationAccountUseCase
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.OAuth2AccountLoader
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class OAuth2LoginServiceTest :
	StringSpec({
		val accountLoader = mockk<OAuth2AccountLoader>()
		val findCertificationAccountUseCase = mockk<FindCertificationAccountUseCase>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2LoginService = OAuth2LoginService(accountLoader, findCertificationAccountUseCase, generateAuthTokenUseCase)

		"login을 수행한다" {
			every { accountLoader.get(any()) } returns FixtureBuilder.give<ProviderAccount>().sample()
			every { findCertificationAccountUseCase.getById(any()) } returns
				AccountResult(FixtureBuilder.give<Account>().sample())
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
	})