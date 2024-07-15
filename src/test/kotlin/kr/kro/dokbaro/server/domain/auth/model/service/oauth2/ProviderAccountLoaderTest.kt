package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.fixture.FixtureBuilder
import kr.kro.dokbaro.server.global.AuthProvider

class ProviderAccountLoaderTest :
	StringSpec({

		val targetProvider = AuthProvider.KAKAO

		val providerAccountLoader =
			ProviderAccountLoader(
				mapOf(
					"${targetProvider.name.lowercase()}ResourceTokenLoader" to
						LoadProviderResourceTokenPort {
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
					ProviderAuthorizationCommand(targetProvider, "authorizeToken"),
				)

			result.provider shouldBe targetProvider
			result.id.isNotBlank() shouldBe true
		}
	})