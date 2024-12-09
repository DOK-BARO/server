package kr.kro.dokbaro.server.core.member.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.ModifyMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.InsertMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.service.exception.AlreadyRegisteredEmailException
import kr.kro.dokbaro.server.core.member.application.service.exception.NotFoundMemberException
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.fixture.FixtureBuilder
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import java.util.UUID
import kotlin.random.Random

class MemberServiceTest :
	StringSpec({
		val insertMemberPort = mockk<InsertMemberPort>()
		val updateMemberPort = UpdateMemberPortMock()
		val existMemberEmailPort = mockk<ExistMemberByEmailPort>()
		val loadMemberByCertificationIdPort = mockk<LoadMemberByCertificationIdPort>()

		val memberService =
			MemberService(insertMemberPort, updateMemberPort, existMemberEmailPort, loadMemberByCertificationIdPort)

		afterEach {
			updateMemberPort.clear()
			clearAllMocks()
		}

		"저장을 수행한다" {
			val command =
				RegisterMemberCommand(
					nickname = "asdf",
					email = "kkk@gmail.com",
					profileImage = "profile.png",
				)

			val member =
				Member(
					nickname = command.nickname,
					email = Email(command.email),
					profileImage = command.profileImage,
					certificationId = UUID.randomUUID(),
					id = Random.nextLong(),
				)

			every { insertMemberPort.insert(any()) } returns member
			every { existMemberEmailPort.existByEmail(command.email) } returns false

			memberService.register(command) shouldBe member
		}

		"중복된 이메일로 저장 시 예외를 반환한다" {
			val command =
				RegisterMemberCommand(
					nickname = "asdf",
					email = "kkk@gmail.com",
					profileImage = "profile.png",
				)
			every { existMemberEmailPort.existByEmail(command.email) } returns true

			shouldThrow<AlreadyRegisteredEmailException> {
				memberService.register(command)
			}
		}

		"수정을 수행한다" {
			val targetUUID = UUID.randomUUID()
			val resentEmail = Email("dasf@kkk.com")
			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns
				memberFixture(certificationId = targetUUID, email = resentEmail)

			val command =
				ModifyMemberCommand(
					targetUUID,
					"newNick",
					"new@new.com",
					"hello.png",
				)

			memberService.modify(command)

			val result = updateMemberPort.storage!!

			result.nickname shouldBe command.nickname
			result.email shouldBe Email(command.email!!)
			result.profileImage shouldBe command.profileImage
		}

		"이메일을 제외하고 수정을 수행한다" {
			val targetUUID = UUID.randomUUID()
			val resentEmail = Email("dasf@kkk.com")
			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns
				memberFixture(certificationId = targetUUID, email = resentEmail)

			val command =
				ModifyMemberCommand(
					targetUUID,
					"newNick",
					null,
					"hello.png",
				)

			memberService.modify(command)

			val result = updateMemberPort.storage!!

			result.nickname shouldBe command.nickname
			result.email shouldBe resentEmail
			result.profileImage shouldBe command.profileImage
		}

		"수정 시 certificationId를 통한 member가 없으면 예외를 반환한다" {
			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns null

			shouldThrow<NotFoundMemberException> {
				memberService.modify(FixtureBuilder.give<ModifyMemberCommand>().sample())
			}
		}

		"회원 탈퇴를 수행한다" {
			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns null

			shouldThrow<NotFoundMemberException> {
				memberService.withdrawBy(UUID.randomUUID())
			}

			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns memberFixture()

			memberService.withdrawBy(UUID.randomUUID())

			updateMemberPort.storage!!.withdraw shouldBe true
		}
	})