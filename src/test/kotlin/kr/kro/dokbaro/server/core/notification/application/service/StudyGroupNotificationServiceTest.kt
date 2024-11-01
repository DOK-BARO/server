package kr.kro.dokbaro.server.core.notification.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewStudyGroupMemberNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationPort
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult

class StudyGroupNotificationServiceTest :
	StringSpec({
		val insertNotificationPort = mockk<InsertNotificationPort>()
		val insertNotificationVisibilityPort = InsertNotificationVisibilityPortMock()
		val findAllStudyGroupMembersUseCase = mockk<FindAllStudyGroupMembersUseCase>()

		val studyGroupService =
			StudyGroupNotificationService(
				insertNotificationPort,
				insertNotificationVisibilityPort,
				findAllStudyGroupMembersUseCase,
			)

		afterEach {
			insertNotificationVisibilityPort.clear()
		}

		"스터디 그룹에 신규 맴버 가입 시 전 맴버에게 알림을 생성한다" {
			every { insertNotificationPort.insert(any()) } returns 1
			every { findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(any()) } returns
				listOf(
					StudyGroupMemberResult(
						groupId = 1L,
						groupMemberId = 101L,
						memberId = 1001L,
						nickname = "Alice",
						role = StudyMemberRole.LEADER,
					),
					StudyGroupMemberResult(
						groupId = 1L,
						groupMemberId = 102L,
						memberId = 1002L,
						nickname = "Bob",
						role = StudyMemberRole.MEMBER,
					),
					StudyGroupMemberResult(
						groupId = 1L,
						groupMemberId = 103L,
						memberId = 1003L,
						nickname = "Charlie",
						role = StudyMemberRole.MEMBER,
					),
				)

			studyGroupService.createNewStudyGroupMemberNotification(
				CreateNewStudyGroupMemberNotificationCommand(
					studyGroupId = 1L,
					studyGroupName = "Chemistry Wizards",
					memberId = 1003L,
					memberName = "Charlie",
				),
			)

			insertNotificationVisibilityPort.storage.size shouldBe 3
		}
	})