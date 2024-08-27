package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAuthorizationServerUrlPort

class LoadOAuth2AuthorizeUrlServiceTest :
	StringSpec({
		val loadUrlPort = mockk<LoadOAuth2ProviderAuthorizationServerUrlPort>()

		val targetProvider = AuthProvider.KAKAO
		val loadOAuth2AuthorizeUrlService =
			LoadOAuth2AuthorizeUrlService(loadUrlPort)

		"provider에 해당하는 authorize url을 반환한다" {
			every { loadUrlPort.getUrl(any(), any()) } returns "https://localhost:8080/api/bash"

			loadOAuth2AuthorizeUrlService.getUrl(targetProvider, "https://localhost:5173").isNotEmpty() shouldBe true
		}
	})