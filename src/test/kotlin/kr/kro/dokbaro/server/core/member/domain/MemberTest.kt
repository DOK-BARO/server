package kr.kro.dokbaro.server.core.member.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class MemberTest :
	StringSpec({

		"이메일 체크를 수행한다" {
			val member =
				Member(
					nickName = "asdf",
					email = Email("asdf@example.com"),
					profileImage = "kk.png",
					certificationId = UUID.randomUUID(),
				)
			member.checkEmail()

			member.email.verified shouldBe true
		}
	})