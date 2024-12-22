package kr.kro.dokbaro.server.core.studygroup.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.ChangeStudyGroupLeaderCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.UpdateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.WithdrawStudyGroupMemberCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.DeleteStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.service.auth.StudyGroupAuthorityCheckService
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.dummy.EventPublisherDummy
import kr.kro.dokbaro.server.fixture.domain.dokbaroUserFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture

class StudyGroupServiceTest :
	StringSpec({
		val insertStudyGroupPort = mockk<InsertStudyGroupPort>()
		val inviteCodeGenerator = RandomSixDigitInviteCodeGenerator()
		val loadStudyGroupPort = mockk<LoadStudyGroupPort>()
		val updateStudyGroupPort = UpdateStudyGroupPortMock()
		val deleteStudyGroupPort = mockk<DeleteStudyGroupPort>()
		val studyGroupAuthorityCheckService = mockk<StudyGroupAuthorityCheckService>()

		val studyGroupService =
			StudyGroupService(
				insertStudyGroupPort,
				inviteCodeGenerator,
				loadStudyGroupPort,
				updateStudyGroupPort,
				EventPublisherDummy(),
				deleteStudyGroupPort,
				studyGroupAuthorityCheckService,
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
			every { loadStudyGroupPort.findBy(any()) } returns studyGroupFixture()
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
			every { loadStudyGroupPort.findBy(any()) } returns null

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
			every { studyGroupAuthorityCheckService.checkDeleteStudyGroup(any(), any()) } returns Unit
			studyGroupService.deleteStudyGroup(1, dokbaroUserFixture())

			verify { deleteStudyGroupPort.deleteStudyGroup(any()) }
		}

		"수정을 수행한다" {
			every { loadStudyGroupPort.findBy(any()) } returns studyGroupFixture(id = 1)
			every { studyGroupAuthorityCheckService.checkUpdateStudyGroup(any(), any()) } returns Unit

			studyGroupService.update(
				UpdateStudyGroupCommand(
					1,
					"asdf",
					"adfs",
					"adf",
				),
				dokbaroUserFixture(),
			)

			updateStudyGroupPort.storage!!.id shouldBe 1
		}

		"수정 수행 시 ID에 해당하는 StudyGroup을 찾을 수 없으면 예외를 발생한다" {
			every { loadStudyGroupPort.findBy(any()) } returns null

			shouldThrow<NotFoundStudyGroupException> {
				studyGroupService.update(
					UpdateStudyGroupCommand(
						1,
						"asdf",
						"adfs",
						"adf",
					),
					dokbaroUserFixture(),
				)
			}
		}

		"스터디 리더를 변경한다" {
			every { loadStudyGroupPort.findBy(any()) } returns studyGroupFixture(id = 1)
			every { studyGroupAuthorityCheckService.checkUpdateStudyGroup(any(), any()) } returns Unit

			studyGroupService.changeStudyGroupLeader(
				ChangeStudyGroupLeaderCommand(
					1,
					1,
				),
				dokbaroUserFixture(),
			)

			updateStudyGroupPort.storage!!.id shouldBe 1
		}

		"스터디 리더 변경 시 StudyGroup을 찾을 수 없으면 예외를 발생한다" {
			every { loadStudyGroupPort.findBy(any()) } returns null

			shouldThrow<NotFoundStudyGroupException> {
				studyGroupService.changeStudyGroupLeader(
					ChangeStudyGroupLeaderCommand(
						1,
						1,
					),
					dokbaroUserFixture(),
				)
			}
		}

		"스터디 탈퇴를 수행한다" {
			every { loadStudyGroupPort.findBy(any()) } returns studyGroupFixture(id = 1)
			every { studyGroupAuthorityCheckService.checkWithdrawMember(any(), any(), any()) } returns Unit

			studyGroupService.withdraw(
				WithdrawStudyGroupMemberCommand(
					1,
					3,
				),
				dokbaroUserFixture(id = 6),
			)

			updateStudyGroupPort.storage!!.id shouldBe 1
		}

		"스터디 탈퇴를 수행 시 StudyGroup을 찾을 수 없으면 예외를 발생한다" {
			every { loadStudyGroupPort.findBy(any()) } returns null

			shouldThrow<NotFoundStudyGroupException> {
				studyGroupService.withdraw(
					WithdrawStudyGroupMemberCommand(
						1,
						1,
					),
					dokbaroUserFixture(),
				)
			}
		}
	})