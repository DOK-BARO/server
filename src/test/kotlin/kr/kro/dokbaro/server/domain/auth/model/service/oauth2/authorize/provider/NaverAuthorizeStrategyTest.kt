package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.provider

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NaverAuthorizeStrategyTest :
	StringSpec({
		val naverAuthorizeStrategy = NaverAuthorizeStrategy("", "", "", "")
		"url을 생성한다" {
			naverAuthorizeStrategy.getUri().isNotEmpty() shouldBe true
		}
	})