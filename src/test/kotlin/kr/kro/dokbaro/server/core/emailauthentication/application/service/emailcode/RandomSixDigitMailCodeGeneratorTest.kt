package kr.kro.dokbaro.server.core.emailauthentication.application.service.emailcode

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RandomSixDigitMailCodeGeneratorTest :
	StringSpec({
		val generator = RandomSixDigitMailCodeGenerator()
		"random으로 6자리 문자 코드를 생성한다" {
			generator.generate().length shouldBe 6
		}
	})