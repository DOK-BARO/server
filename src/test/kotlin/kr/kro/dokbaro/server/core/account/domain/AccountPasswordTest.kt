package kr.kro.dokbaro.server.core.account.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AccountPasswordTest :
	StringSpec({

		val encoder = BCryptPasswordEncoder()

		"password 변경을 수행한다" {
			val beforePassword = "before"
			val afterPassword = "after"

			val password =
				EmailAccount.of(
					email = "hello@example.com",
					rawPassword = beforePassword,
					memberId = 1,
					encoder = encoder,
				)

			password.changePassword(afterPassword, encoder)

			password.match(afterPassword, encoder) shouldBe true
		}

		"password 일치 여부를 확인한다" {

			val passwordValue = "password"

			val password =
				EmailAccount.of(
					email = "hello@example.com",
					rawPassword = passwordValue,
					memberId = 1,
					encoder = encoder,
				)

			password.match(passwordValue, encoder) shouldBe true
			password.match("wrong", encoder) shouldBe false
		}
	})