package kr.kro.dokbaro.server.core.notification.adapter.input.event

import kr.kro.dokbaro.server.core.bookquiz.event.CreatedQuizEvent
import kr.kro.dokbaro.server.core.bookquiz.event.UpdatedQuizEvent
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewQuizNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewReviewNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewStudyGroupMemberNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.CreateUpdateQuizNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewQuizNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewReviewNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewStudyGroupMemberNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateUpdateQuizNotificationCommand
import kr.kro.dokbaro.server.core.quizreview.event.CreatedQuizReviewEvent
import kr.kro.dokbaro.server.core.studygroup.event.JoinedStudyGroupMemberEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CreateNotificationEventListener(
	private val createNewQuizNotificationUseCase: CreateNewQuizNotificationUseCase,
	private val createNewReviewNotificationUseCase: CreateNewReviewNotificationUseCase,
	private val createNewStudyGroupMemberNotificationUseCase: CreateNewStudyGroupMemberNotificationUseCase,
	private val updateQuizNotificationUseCase: CreateUpdateQuizNotificationUseCase,
) {
	@EventListener
	fun subscribeCreatedQuizEvent(event: CreatedQuizEvent) {
		createNewQuizNotificationUseCase.createNewQuizNotification(
			CreateNewQuizNotificationCommand(
				quizId = event.quizId,
				creatorId = event.creatorId,
				creatorName = event.creatorName,
				studyGroupId = event.studyGroupId,
			),
		)
	}

	@EventListener
	fun subscribeCreatedQuizReviewEvent(event: CreatedQuizReviewEvent) {
		createNewReviewNotificationUseCase.createNewReviewNotification(
			CreateNewReviewNotificationCommand(
				quizId = event.quizId,
				reviewId = event.reviewId,
				quizCreatorId = event.quizCreatorId,
			),
		)
	}

	@EventListener
	fun subscribeJoinedStudyGroupMemberEvent(event: JoinedStudyGroupMemberEvent) {
		createNewStudyGroupMemberNotificationUseCase.createNewStudyGroupMemberNotification(
			CreateNewStudyGroupMemberNotificationCommand(
				studyGroupId = event.studyGroupId,
				studyGroupName = event.studyGroupName,
				memberId = event.memberId,
				memberName = event.memberName,
			),
		)
	}

	@EventListener
	fun subscribeUpdatedQuizEvent(event: UpdatedQuizEvent) {
		updateQuizNotificationUseCase.createUpdateQuizNotification(
			CreateUpdateQuizNotificationCommand(
				quizId = event.quizId,
				quizTitle = event.quizTitle,
				quizCreatorId = event.quizCreatorId,
			),
		)
	}
}