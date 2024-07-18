package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class KakaoAccountLoaderTest :
	StringSpec({
		val kakaoResourceClient = mockk<KakaoResourceClient>()
		val kakaoAccountLoader = KakaoAccountLoader(kakaoResourceClient)

		" kakao account를 불러온다" {
			val sample =
				FixtureBuilder
					.give<KakaoAccount>()
					.sample()

			every { kakaoResourceClient.getUserProfiles(any()) } returns sample

			val result: ProviderAccount = kakaoAccountLoader.getAttributes("accessToken")

			result shouldNotBe null
			result.id shouldNotBe null
		}
	})