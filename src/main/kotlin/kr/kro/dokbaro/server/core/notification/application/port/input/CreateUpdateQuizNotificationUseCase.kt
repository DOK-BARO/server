package kr.kro.dokbaro.server.core.notification.application.port.input

import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateUpdateQuizNotificationCommand

fun interface CreateUpdateQuizNotificationUseCase {
	fun createUpdateQuizNotification(command: CreateUpdateQuizNotificationCommand)
}