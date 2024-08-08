package kr.kro.dokbaro.server.core.auth.adapter.out.google

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.GoogleAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource.GoogleAccount
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class GoogleAccountLoaderTest :
	StringSpec({
		val googleResourceClient = mockk<GoogleResourceClient>()
		val googleAccountLoader = GoogleAccountLoader(googleResourceClient)

		" google account를 불러온다" {
			val sample =
				FixtureBuilder
					.give<GoogleAccount>()
					.sample()

			every { googleResourceClient.getUserProfiles(any()) } returns sample

			val result: ProviderAccount = googleAccountLoader.getAttributes("accessToken")

			result shouldNotBe null
			result.id shouldNotBe null
		}
	})