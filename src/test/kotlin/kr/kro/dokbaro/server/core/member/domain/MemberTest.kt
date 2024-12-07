package kr.kro.dokbaro.server.core.member.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class MemberTest :
	StringSpec({

		"수정을 수행한다" {
			val member =
				Member(
					nickname = "asdf",
					email = Email("asdf@example.com"),
					profileImage = "kk.png",
					certificationId = UUID.randomUUID(),
				)

			val newNickName = "newNickname"
			val newEmail = Email("newEmail@gmail.com")
			val newImage = "new.png"
			member.modify(
				nickName = newNickName,
				email = newEmail,
				profileImage = newImage,
			)

			member.nickname shouldBe newNickName
			member.email shouldBe newEmail
			member.profileImage shouldBe newImage
		}

		"수정 시 null 값을 전송하면 기존 값을 유지한다" {
			val beforeNickName = "name"
			val beforeEmail = Email("mail@gmail.com")
			val beforeImage = "before.png"

			val member =
				Member(
					nickname = beforeNickName,
					email = beforeEmail,
					profileImage = beforeImage,
					certificationId = UUID.randomUUID(),
				)

			member.modify(
				nickName = null,
				email = null,
				profileImage = null,
			)

			member.nickname shouldBe beforeNickName
			member.email shouldBe beforeEmail
			member.profileImage shouldBe beforeImage
		}
	})