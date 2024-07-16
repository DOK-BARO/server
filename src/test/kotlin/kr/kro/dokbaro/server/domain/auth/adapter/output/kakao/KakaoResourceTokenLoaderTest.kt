package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.AuthorizationTokenResponse
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoAuthorizationClient
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class KakaoResourceTokenLoaderTest :
	StringSpec({
		val kakaoAuthorizationClient = mockk<KakaoAuthorizationClient>()
		val kakaoResourceTokenLoader = KakaoResourceTokenLoader(kakaoAuthorizationClient, "token", "aa", "bb", "cc")

		"kakao의 accessToken을 발급받는다" {
			val sample =
				FixtureBuilder
					.give<AuthorizationTokenResponse>()
					.setExp(
						AuthorizationTokenResponse::accessToken,
						"asdfasdf",
					).sample()
			every { kakaoAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns sample

			val result: String = kakaoResourceTokenLoader.getToken("token")

			result.isNotEmpty() shouldBe true
		}
	})