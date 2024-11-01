package kr.kro.dokbaro.server.core.notification.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewQuizNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewReviewNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateUpdateQuizNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationPort
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult

class QuizNotificationServiceTest :
	StringSpec({
		val insertNotificationPort = mockk<InsertNotificationPort>()
		val insertNotificationVisibilityPort = InsertNotificationVisibilityPortMock()
		val findAllStudyGroupMembersUseCase = mockk<FindAllStudyGroupMembersUseCase>()

		val quizNotificationService =
			QuizNotificationService(
				insertNotificationPort,
				insertNotificationVisibilityPort,
				findAllStudyGroupMembersUseCase,
			)

		afterEach {
			clearAllMocks()
			insertNotificationVisibilityPort.clear()
		}

		"신규 퀴즈 생성 시 관련 스터디원들에게 알림을 생성한다" {
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

			quizNotificationService.createNewQuizNotification(
				CreateNewQuizNotificationCommand(
					quizId = 1L,
					creatorId = 1001L,
					creatorName = "Alice",
					studyGroupId = 200L,
				),
			)

			insertNotificationVisibilityPort.storage.size shouldBe 3
		}

		"신규 퀴즈가 studyGroup 에 속해있지 않으면 별도 알림을 생성하지 않는다" {
			quizNotificationService.createNewQuizNotification(
				CreateNewQuizNotificationCommand(
					quizId = 1L,
					creatorId = 1001L,
					creatorName = "Alice",
					studyGroupId = null,
				),
			)
			verify(exactly = 0) { insertNotificationPort.insert(any()) }
			insertNotificationVisibilityPort.storage.size shouldBe 0
		}

		"신규 리뷰 생성 시 퀴즈 제작자에게 알림을 생성한다" {
			every { insertNotificationPort.insert(any()) } returns 1

			quizNotificationService.createNewReviewNotification(CreateNewReviewNotificationCommand(1, 1, 1))

			insertNotificationVisibilityPort.storage.size shouldBe 1
		}

		"퀴즈 업데이트 시 퀴즈 제작자에게 알림을 생성한다" {
			every { insertNotificationPort.insert(any()) } returns 1

			quizNotificationService.createUpdateQuizNotification(CreateUpdateQuizNotificationCommand(1, "hello", 1))

			insertNotificationVisibilityPort.storage.size shouldBe 1
		}
	})