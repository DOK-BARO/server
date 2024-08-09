package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAuthorizationServerUrlPort

class LoadOAuth2AuthorizeUrlServiceTest :
	StringSpec({
		val loadUrlPort = mockk<LoadProviderAuthorizationServerUrlPort>()

		val targetProvider = AuthProvider.KAKAO
		val loadOAuth2AuthorizeUrlService =
			LoadOAuth2AuthorizeUrlService(loadUrlPort)

		"provider에 해당하는 authorize url을 반환한다" {
			every { loadUrlPort.getUrl(any()) } returns "https://localhost:8080/api/bash"

			loadOAuth2AuthorizeUrlService.get(targetProvider).isNotEmpty() shouldBe true
		}
	})