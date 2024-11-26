package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.common.http.CookieGenerator
import kr.kro.dokbaro.server.common.http.jwt.JwtCookiePairGenerator
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseEntityGenerator
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

class JwtResponseGeneratorTest :
	StringSpec({

		val cookieGenerator = CookieGenerator("localhost")

		val jwtCookiePairGenerator =
			JwtCookiePairGenerator(
				cookieGenerator,
				"Authorization",
				"Refresh",
				15,
				7,
			)
		val jwtResponseEntityGenerator = JwtResponseEntityGenerator(jwtCookiePairGenerator)

		"jwt token 값을 넣어 cookie를 header에 주입한다" {
			val responseBuilder: ResponseEntity.BodyBuilder =
				jwtResponseEntityGenerator.getResponseBuilder(
					"access_token",
					"refresh_token",
				)

			val responseHeaders = responseBuilder.build<Void>().headers
			responseHeaders[HttpHeaders.SET_COOKIE].isNullOrEmpty() shouldBe false
		}
	})