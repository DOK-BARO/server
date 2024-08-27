package kr.kro.dokbaro.server.core.member.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.SaveMemberPort
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import java.util.UUID
import kotlin.random.Random

class MemberServiceTest :
	StringSpec({
		val saveMemberPort = mockk<SaveMemberPort>()
		val updateMemberPort = UpdateMemberPortMock()
		val existMemberEmailPort = mockk<ExistMemberByEmailPort>()
		val loadMemberByCertificationIdPort = mockk<LoadMemberByCertificationIdPort>()

		val memberService =
			MemberService(saveMemberPort, updateMemberPort, existMemberEmailPort, loadMemberByCertificationIdPort)

		"저장을 수행한다" {
			val command =
				RegisterMemberCommand(
					nickName = "asdf",
					email = "kkk@gmail.com",
					profileImage = "profile.png",
				)

			val member =
				Member(
					nickName = command.nickName,
					email = Email(command.email),
					profileImage = command.profileImage,
					certificationId = UUID.randomUUID(),
					id = Random.nextLong(),
				)

			every { saveMemberPort.save(any()) } returns member
			every { existMemberEmailPort.existByEmail(command.email) } returns false

			memberService.register(command) shouldBe member
		}

		"중복된 이메일로 저장 시 예외를 반환한다" {
			val command =
				RegisterMemberCommand(
					nickName = "asdf",
					email = "kkk@gmail.com",
					profileImage = "profile.png",
				)
			every { existMemberEmailPort.existByEmail(command.email) } returns true

			shouldThrow<AlreadyRegisteredEmailException> {
				memberService.register(command)
			}
		}

		"메일 검증을 수행한다" {
			val targetUUID = UUID.randomUUID()

			every { loadMemberByCertificationIdPort.findByCertificationId(targetUUID) } returns
				Member(
					nickName = "nickname",
					email = Email("dasf"),
					profileImage = "image.png",
					certificationId = targetUUID,
					id = Random.nextLong(),
				)

			memberService.checkMail(targetUUID)

			updateMemberPort.storage!!.email.verified shouldBe true
		}

		"메일 검증 시 certificationId를 통한 member가 없으면 예외를 반환한다" {
			every { loadMemberByCertificationIdPort.findByCertificationId(any()) } returns null

			shouldThrow<NotFoundMemberException> {
				memberService.checkMail(UUID.randomUUID())
			}
		}
	})