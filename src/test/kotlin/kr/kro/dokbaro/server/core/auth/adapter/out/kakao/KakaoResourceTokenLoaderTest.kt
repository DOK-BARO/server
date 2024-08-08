package kr.kro.dokbaro.server.core.auth.adapter.out.kakao

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.KakaoResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoAuthorizationTokenResponse
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class KakaoResourceTokenLoaderTest :
	StringSpec({
		val kakaoAuthorizationClient = mockk<KakaoAuthorizationClient>()
		val kakaoResourceTokenLoader = KakaoResourceTokenLoader(kakaoAuthorizationClient, "token", "bb", "cc")

		"kakao의 accessToken을 발급받는다" {
			val sample =
				FixtureBuilder
					.give<KakaoAuthorizationTokenResponse>()
					.setExp(
						KakaoAuthorizationTokenResponse::accessToken,
						"asdfasdf",
					).sample()
			every { kakaoAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns sample

			val result: String = kakaoResourceTokenLoader.getToken("token", "http://localhost:5173/oauth2/redirected/kakao")

			result.isNotEmpty() shouldBe true
		}
	})