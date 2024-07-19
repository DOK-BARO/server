package kr.kro.dokbaro.server.domain.auth.adapter.output.naver

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverAuthorizationTokenResponse
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class NaverResourceTokenLoaderTest :
	StringSpec({
		val naverAuthorizationClient = mockk<NaverAuthorizationClient>()
		val naverResourceTokenLoader = NaverResourceTokenLoader(naverAuthorizationClient, "token", "bb", "cc")

		"naver의 accessToken을 발급받는다" {
			val sample =
				FixtureBuilder
					.give<NaverAuthorizationTokenResponse>()
					.setExp(
						NaverAuthorizationTokenResponse::accessToken,
						"asdfasdf",
					).sample()
			every { naverAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns sample

			val result: String = naverResourceTokenLoader.getToken("token", "http://localhost:5173/oauth2/redirected/naver")

			result.isNotEmpty() shouldBe true
		}
	})