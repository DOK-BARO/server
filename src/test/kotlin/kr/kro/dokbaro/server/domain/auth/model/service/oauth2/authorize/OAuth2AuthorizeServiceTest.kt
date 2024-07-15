package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.global.AuthProvider

class OAuth2AuthorizeServiceTest :
	StringSpec({
		val targetProvider = AuthProvider.KAKAO
		val oAuth2AuthorizeService =
			OAuth2AuthorizeService(
				mapOf("${targetProvider.name.lowercase()}AuthorizeStrategy" to OAuth2AuthorizeStrategy { "http://localhost" }),
			)

		"provider에 해당하는 authorize url을 반환한다" {
			oAuth2AuthorizeService.get(targetProvider).isNotEmpty() shouldBe true
		}
	})