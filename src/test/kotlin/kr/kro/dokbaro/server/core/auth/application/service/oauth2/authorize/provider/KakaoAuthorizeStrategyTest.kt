package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.provider

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class KakaoAuthorizeStrategyTest :
	StringSpec({
		val kakaoAuthorizeStrategy = KakaoAuthorizeStrategy("", "", "", "", "")
		"url을 생성한다" {
			kakaoAuthorizeStrategy.getUri().isNotEmpty() shouldBe true
		}
	})