package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderResourceTokenPort
import kr.kro.dokbaro.server.fixture.domain.oAuth2ProviderAccountFixture

class OAuth2ProviderAccountLoaderTest :
	StringSpec({
		val loadProviderResourceTokenPort = mockk<LoadOAuth2ProviderResourceTokenPort>()
		val loadProviderAccountPort = mockk<LoadOAuth2ProviderAccountPort>()

		val targetProvider = AuthProvider.KAKAO

		val accountLoader =
			OAuth2ProviderAccountLoader(
				loadProviderResourceTokenPort,
				loadProviderAccountPort,
			)

		"provider에 해당하는 account를 가져온다" {
			every { loadProviderResourceTokenPort.getToken(targetProvider, any(), any()) } returns "token"
			every {
				loadProviderAccountPort.getProviderAccount(
					targetProvider,
					any(),
				)
			} returns oAuth2ProviderAccountFixture()
			val result: OAuth2ProviderAccount =
				accountLoader.getAccount(
					LoadProviderAccountCommand(
						targetProvider,
						"authorizeToken",
						"http://localhost:5173/oauth2/redirected/kakao",
					),
				)

			result.provider shouldBe targetProvider
			result.id.isNotBlank() shouldBe true
		}
	})