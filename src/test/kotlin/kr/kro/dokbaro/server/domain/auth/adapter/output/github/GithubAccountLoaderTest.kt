package kr.kro.dokbaro.server.domain.auth.adapter.output.github

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.GithubResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.provideraccount.GithubAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class GithubAccountLoaderTest :
	StringSpec({
		val githubResourceClient = mockk<GithubResourceClient>()
		val githubAccountLoader = GithubAccountLoader(githubResourceClient)

		" github account를 불러온다" {
			val sample =
				FixtureBuilder
					.give<GithubAccount>()
					.sample()

			every { githubResourceClient.getUserProfiles(any()) } returns sample

			val result: ProviderAccount = githubAccountLoader.getAttributes("accessToken")

			result shouldNotBe null
			result.id shouldNotBe null
		}
	})