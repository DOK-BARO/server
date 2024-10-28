package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.dto.CertificatedMember
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupByInviteCodePort
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import java.util.UUID

class StudyGroupServiceTest :
	StringSpec({
		val insertStudyGroupPort = mockk<InsertStudyGroupPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val inviteCodeGenerator = RandomSixDigitInviteCodeGenerator()
		val loadStudyGroupByInviteCodePort = mockk<LoadStudyGroupByInviteCodePort>()
		val updateStudyGroupPort = UpdateStudyGroupPortMock()

		val studyGroupService =
			StudyGroupService(
				insertStudyGroupPort,
				findCertificatedMemberUseCase,
				inviteCodeGenerator,
				loadStudyGroupByInviteCodePort,
				updateStudyGroupPort,
			)

		afterEach {
			updateStudyGroupPort.clear()
			clearAllMocks()
		}

		"study group을 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns
				CertificatedMember(
					nickName = "test",
					email = "test@test.com",
					profileImage = "test.png",
					certificationId = UUID.randomUUID(),
					roles = setOf(Role.GUEST),
					id = 1,
				)

			every { insertStudyGroupPort.insert(any()) } returns 1

			val command =
				CreateStudyGroupCommand(
					name = "test",
					introduction = "test",
					profileImageUrl = "profile.png",
					creatorAuthId = UUID.randomUUID(),
				)

			studyGroupService.create(command) shouldNotBe null
		}

		"study group에 참여한다" {
			every { loadStudyGroupByInviteCodePort.findByInviteCode(any()) } returns studyGroupFixture()
			val member = certificatedMemberFixture()
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns member

			studyGroupService.join(
				JoinStudyGroupCommand(
					"abc111",
					UUID.randomUUID(),
				),
			)

			updateStudyGroupPort.storage!!
				.studyMembers
				.map { it.memberId }
				.shouldContain(member.id)
		}

		"스터디 그룹 참여 시 초대코드에 맞는 그룹을 찾을 수 없으면 예외를 발생한다" {
			every { loadStudyGroupByInviteCodePort.findByInviteCode(any()) } returns null

			shouldThrow<NotFoundStudyGroupException> {
				studyGroupService.join(
					JoinStudyGroupCommand(
						"abc111",
						UUID.randomUUID(),
					),
				)
			}
		}
	})