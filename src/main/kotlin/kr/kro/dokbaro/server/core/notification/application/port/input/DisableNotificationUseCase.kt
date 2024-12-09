package kr.kro.dokbaro.server.core.notification.application.port.input

interface DisableNotificationUseCase {
	fun disableBy(
		notificationId: Long,
		memberId: Long,
	)
}