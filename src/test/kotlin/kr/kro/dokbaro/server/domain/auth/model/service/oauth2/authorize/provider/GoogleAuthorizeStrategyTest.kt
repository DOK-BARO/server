package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.provider

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GoogleAuthorizeStrategyTest :
	StringSpec({
		val googleAuthorizeStrategy = GoogleAuthorizeStrategy("", "", "")
		"url을 생성한다" {
			googleAuthorizeStrategy.getUri().isNotEmpty() shouldBe true
		}
	})