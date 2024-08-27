package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CookieGeneratorTest :
	StringSpec({
		val cookieGenerator = CookieGenerator("localhost")

		"cookie 를 생성한다 " {
			val cookie: String = cookieGenerator.generate("name", "value", 50)

			cookie.isNotEmpty() shouldBe true
		}
	})