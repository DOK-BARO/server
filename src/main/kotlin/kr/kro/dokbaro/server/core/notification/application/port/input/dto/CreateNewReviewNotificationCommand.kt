package kr.kro.dokbaro.server.core.notification.application.port.input.dto

data class CreateNewReviewNotificationCommand(
	val quizId: Long,
	val reviewId: Long,
	val quizCreatorId: Long,
)