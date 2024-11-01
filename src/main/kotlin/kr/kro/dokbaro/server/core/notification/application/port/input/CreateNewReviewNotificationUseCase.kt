package kr.kro.dokbaro.server.core.notification.application.port.input

import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewReviewNotificationCommand

fun interface CreateNewReviewNotificationUseCase {
	fun createNewReviewNotification(command: CreateNewReviewNotificationCommand)
}