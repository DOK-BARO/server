package kr.kro.dokbaro.server.core.notification.application.port.input

import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewQuizNotificationCommand

fun interface CreateNewQuizNotificationUseCase {
	fun createNewQuizNotification(command: CreateNewQuizNotificationCommand)
}