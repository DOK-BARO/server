package kr.kro.dokbaro.server.core.account.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.account.domain.exception.PasswordNotMatchException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AccountPasswordTest :
	StringSpec({

		val encoder = BCryptPasswordEncoder()

		"password 변경을 수행한다" {
			val beforePassword = "before"
			val afterPassword = "after"

			val password =
				AccountPassword.of(
					rawPassword = beforePassword,
					memberId = 1,
					encoder = encoder,
				)

			password.changePassword(beforePassword, afterPassword, encoder)

			password.match(afterPassword, encoder) shouldBe true
		}

		"password 변경 시 이전 비밀번호와 일치하지 않으면 예외를 반환한디" {

			val beforePassword = "before"
			val afterPassword = "after"

			val password =
				AccountPassword.of(
					rawPassword = beforePassword,
					memberId = 1,
					encoder = encoder,
				)

			shouldThrow<PasswordNotMatchException> {
				password.changePassword("wrong", afterPassword, encoder)
			}
		}
	})