package kr.kro.dokbaro.server.core.notification.application.port.input.dto

data class CreateUpdateQuizNotificationCommand(
	val quizId: Long,
	val quizTitle: String,
	val quizCreatorId: Long,
)