package kr.kro.dokbaro.server.domain.auth.adapter.output.google

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.GoogleAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.GoogleAuthorizationTokenResponse
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class GoogleResourceTokenLoaderTest :
	StringSpec({
		val googleAuthorizationClient = mockk<GoogleAuthorizationClient>()
		val googleResourceTokenLoader = GoogleResourceTokenLoader(googleAuthorizationClient, "token", "bb", "cc")

		"google의 accessToken을 발급받는다" {
			val sample =
				FixtureBuilder
					.give<GoogleAuthorizationTokenResponse>()
					.setExp(
						GoogleAuthorizationTokenResponse::accessToken,
						"asdfasdf",
					).sample()
			every { googleAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns sample

			val result: String = googleResourceTokenLoader.getToken("token", "http://localhost:5173/oauth2/redirected/google")

			result.isNotEmpty() shouldBe true
		}
	})