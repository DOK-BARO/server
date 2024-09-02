package kr.kro.dokbaro.server.core.emailauthentication.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailAuthenticationTest :
	StringSpec({
		val address = "www.example.com"

		"인증된 이메일을 일치 여부 확인 후 사용한다." {
			val code = "123ABC"
			val emailAuthentication =
				EmailAuthentication(
					address = address,
					code = code,
				)

			emailAuthentication.matchCode(code)

			emailAuthentication.use()

			emailAuthentication.authenticated shouldBe true
			emailAuthentication.used shouldBe true
		}

		"메일 사용 시 인증이 되어있지 않으면 예외를 반환한다" {
			val code = "123ABC"
			val emailAuthentication =
				EmailAuthentication(
					address = address,
					code = code,
				)

			shouldThrow<UnauthenticatedEmailException> {
				emailAuthentication.use()
			}
		}

		"코드 일치 확인 시 불일치하면 인증여부를 반환하지 않는다" {
			val code = "123ABC"

			val emailAuthentication =
				EmailAuthentication(
					address = address,
					code = code,
				)
			emailAuthentication.matchCode("KASDFA") shouldBe false
			emailAuthentication.authenticated shouldBe false
		}

		"코드 변경을 수행한다" {
			val newCode = "NEWCOD"
			val emailAuthentication =
				EmailAuthentication(
					address = address,
					code = "BEFORE",
				)

			emailAuthentication.changeCode(newCode)

			emailAuthentication.code shouldBe newCode
		}
	})