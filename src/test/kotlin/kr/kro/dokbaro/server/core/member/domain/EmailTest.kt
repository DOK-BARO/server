package kr.kro.dokbaro.server.core.member.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailTest :
	StringSpec({

		"이메일 검증을 수행하면 검증 여부가 true로 변경한다" {
			val email = Email("qqq@asdf.com")
			val verifyEmail = email.verify()

			email.verified shouldBe false
			verifyEmail.verified shouldBe true
		}
	})