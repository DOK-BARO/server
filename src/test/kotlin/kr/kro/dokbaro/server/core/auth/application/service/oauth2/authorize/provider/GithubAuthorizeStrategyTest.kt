package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.provider

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class GithubAuthorizeStrategyTest :
	StringSpec({
		val githubAuthorizeStrategy = GithubAuthorizeStrategy("", "", "", "")
		"url을 생성한다" {
			githubAuthorizeStrategy.getUri().isNotEmpty() shouldBe true
		}
	})