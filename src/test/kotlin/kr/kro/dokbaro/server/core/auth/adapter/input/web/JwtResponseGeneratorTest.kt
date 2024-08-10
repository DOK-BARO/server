package kr.kro.dokbaro.server.core.auth.adapter.input.web

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

class JwtResponseGeneratorTest :
	StringSpec({

		val cookieGenerator = CookieGenerator("localhost")

		val jwtResponseGenerator = JwtResponseGenerator(cookieGenerator, "Authorization", "Refresh", 500)

		"jwt token 값을 넣어 cookie를 header에 주입한다" {
			val responseBuilder: ResponseEntity.BodyBuilder =
				jwtResponseGenerator.getResponseBuilder(
					"access_token",
					"refresh_token",
				)

			val responseHeaders = responseBuilder.build<Void>().headers
			responseHeaders[HttpHeaders.SET_COOKIE].isNullOrEmpty() shouldBe false
		}
	})