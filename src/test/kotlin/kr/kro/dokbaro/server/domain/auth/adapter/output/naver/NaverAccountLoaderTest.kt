package kr.kro.dokbaro.server.domain.auth.adapter.output.naver

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.resource.NaverAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class NaverAccountLoaderTest :
	StringSpec({
		val naverResourceClient = mockk<NaverResourceClient>()
		val naverAccountLoader = NaverAccountLoader(naverResourceClient)

		" naver account를 불러온다" {
			val sample =
				FixtureBuilder
					.give<NaverAccount>()
					.sample()

			every { naverResourceClient.getUserProfiles(any()) } returns sample

			val result: ProviderAccount = naverAccountLoader.getAttributes("accessToken")

			result shouldNotBe null
			result.id shouldNotBe null
		}
	})