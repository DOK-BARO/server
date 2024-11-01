package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

class InsertNotificationVisibilityPortMock(
	val storage: MutableList<NotificationVisibility> = mutableListOf(),
) : InsertNotificationVisibilityPort {
	override fun insertAll(notificationVisibilities: Collection<NotificationVisibility>) {
		storage.addAll(notificationVisibilities)
	}

	fun clear() {
		storage.clear()
	}
}