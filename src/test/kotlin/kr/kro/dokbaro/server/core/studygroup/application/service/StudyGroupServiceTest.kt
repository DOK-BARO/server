package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.DeleteStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupByInviteCodePort
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.dummy.EventPublisherDummy
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture

class StudyGroupServiceTest :
	StringSpec({
		val insertStudyGroupPort = mockk<InsertStudyGroupPort>()
		val inviteCodeGenerator = RandomSixDigitInviteCodeGenerator()
		val loadStudyGroupByInviteCodePort = mockk<LoadStudyGroupByInviteCodePort>()
		val updateStudyGroupPort = UpdateStudyGroupPortMock()
		val deleteStudyGroupPort = mockk<DeleteStudyGroupPort>()

		val studyGroupService =
			StudyGroupService(
				insertStudyGroupPort,
				inviteCodeGenerator,
				loadStudyGroupByInviteCodePort,
				updateStudyGroupPort,
				EventPublisherDummy(),
				deleteStudyGroupPort,
			)

		afterEach {
			updateStudyGroupPort.clear()
			clearAllMocks()
		}

		"study group을 생성한다" {

			every { insertStudyGroupPort.insert(any()) } returns 1

			val command =
				CreateStudyGroupCommand(
					name = "test",
					introduction = "test",
					profileImageUrl = "profile.png",
					creatorId = 1,
				)

			studyGroupService.create(command) shouldNotBe null
		}

		"study group에 참여한다" {
			every { loadStudyGroupByInviteCodePort.findByInviteCode(any()) } returns studyGroupFixture()
			val member = memberFixture()

			studyGroupService.join(
				JoinStudyGroupCommand(
					"abc111",
					memberId = member.id,
					memberNickname = member.nickname,
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
						1,
						"memberNickname",
					),
				)
			}
		}

		"삭제를 수행한다" {
			every { deleteStudyGroupPort.deleteStudyGroup(any()) } returns Unit

			studyGroupService.deleteStudyGroup(1)

			verify { deleteStudyGroupPort.deleteStudyGroup(any()) }
		}
	})