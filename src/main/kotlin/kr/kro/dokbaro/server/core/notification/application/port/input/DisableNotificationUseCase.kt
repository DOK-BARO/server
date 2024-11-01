package kr.kro.dokbaro.server.core.notification.application.port.input

import java.util.UUID

interface DisableNotificationUseCase {
	fun disableBy(
		notificationId: Long,
		authId: UUID,
	)
}