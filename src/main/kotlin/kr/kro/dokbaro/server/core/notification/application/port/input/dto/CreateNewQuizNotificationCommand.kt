package kr.kro.dokbaro.server.core.notification.application.port.input.dto

data class CreateNewQuizNotificationCommand(
	val quizId: Long,
	val creatorId: Long,
	val creatorName: String,
	val studyGroupId: Long?,
)