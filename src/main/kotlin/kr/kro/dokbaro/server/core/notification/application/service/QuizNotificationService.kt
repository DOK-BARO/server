package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewQuizNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewReviewNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateUpdateQuizNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewQuizNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewReviewNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateUpdateQuizNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationPort
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.domain.Notification
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import kr.kro.dokbaro.server.core.notification.domain.content.NewQuizNotificationContent
import kr.kro.dokbaro.server.core.notification.domain.content.NewReviewNotificationContent
import kr.kro.dokbaro.server.core.notification.domain.content.UpdateQuizNotificationContent
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import org.springframework.stereotype.Service

@Service
class QuizNotificationService(
	private val insertNotificationPort: InsertNotificationPort,
	private val insertNotificationVisibilityPort: InsertNotificationVisibilityPort,
	private val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase,
) : CreateNewQuizNotificationUseCase,
	CreateNewReviewNotificationUseCase,
	CreateUpdateQuizNotificationUseCase {
	override fun createNewQuizNotification(command: CreateNewQuizNotificationCommand) {
		if (command.studyGroupId == null) {
			return
		}

		val savedNotificationId: Long =
			insertNotificationPort.insert(
				Notification(
					content = NewQuizNotificationContent(command.creatorName).getContent(),
					trigger = NotificationTrigger.NEW_QUIZ,
					linkedId = command.quizId,
				),
			)

		val studyGroupMembers = findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(command.studyGroupId)

		insertNotificationVisibilityPort.insertAll(
			studyGroupMembers.map { NotificationVisibility(savedNotificationId, it.memberId) },
		)
	}

	override fun createNewReviewNotification(command: CreateNewReviewNotificationCommand) {
		val savedNotificationId =
			insertNotificationPort.insert(
				Notification(
					content = NewReviewNotificationContent().getContent(),
					trigger = NotificationTrigger.NEW_QUIZ_REVIEW,
					linkedId = command.quizId,
				),
			)

		insertNotificationVisibilityPort.insertAll(
			listOf(NotificationVisibility(savedNotificationId, command.quizCreatorId)),
		)
	}

	override fun createUpdateQuizNotification(command: CreateUpdateQuizNotificationCommand) {
		val savedNotificationId =
			insertNotificationPort.insert(
				Notification(
					content = UpdateQuizNotificationContent(command.quizTitle).getContent(),
					trigger = NotificationTrigger.UPDATE_QUIZ,
					linkedId = command.quizId,
				),
			)

		insertNotificationVisibilityPort.insertAll(
			listOf(NotificationVisibility(savedNotificationId, command.quizCreatorId)),
		)
	}
}