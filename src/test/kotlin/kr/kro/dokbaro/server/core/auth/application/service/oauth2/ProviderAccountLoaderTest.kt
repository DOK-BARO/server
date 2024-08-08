package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder
import kr.kro.dokbaro.server.global.AuthProvider

class ProviderAccountLoaderTest :
	StringSpec({

		val targetProvider = AuthProvider.KAKAO

		val providerAccountLoader =
			ProviderAccountLoader(
				mapOf(
					"${targetProvider.name.lowercase()}ResourceTokenLoader" to
						LoadProviderResourceTokenPort { _, _ ->
							"provider-resource-access-token"
						},
				),
				mapOf(
					"${targetProvider.name.lowercase()}AccountLoader" to
						LoadProviderAccountPort {
							FixtureBuilder
								.give<ProviderAccount>()
								.setExp(ProviderAccount::provider, targetProvider)
								.sample()
						},
				),
			)

		"provider에 해당하는 account를 가져온다" {
			val result: ProviderAccount =
				providerAccountLoader.load(
					ProviderAuthorizationCommand(targetProvider, "authorizeToken", "http://localhost:5173/oauth2/redirected/kakao"),
				)

			result.provider shouldBe targetProvider
			result.id.isNotBlank() shouldBe true
		}
	})