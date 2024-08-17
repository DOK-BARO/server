package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class OAuth2AccountLoaderTest :
	StringSpec({
		val loadProviderResourceTokenPort = mockk<LoadProviderResourceTokenPort>()
		val loadProviderAccountPort = mockk<LoadProviderAccountPort>()

		val targetProvider = AuthProvider.KAKAO

		val accountLoader =
			OAuth2AccountLoader(
				loadProviderResourceTokenPort,
				loadProviderAccountPort,
			)

		"provider에 해당하는 account를 가져온다" {
			every { loadProviderResourceTokenPort.getToken(targetProvider, any(), any()) } returns "token"
			every { loadProviderAccountPort.getProviderAccount(targetProvider, any()) } returns
				FixtureBuilder
					.give<ProviderAccount>()
					.setExp(ProviderAccount::provider, targetProvider)
					.setExp(ProviderAccount::id, "accountId")
					.sample()

			val result: ProviderAccount =
				accountLoader.get(
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