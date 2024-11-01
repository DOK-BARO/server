package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.out.UpdateNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

class UpdateNotificationVisibilityPortMock(
	val storage: MutableMap<Long, NotificationVisibility> = mutableMapOf(),
) : UpdateNotificationVisibilityPort {
	override fun update(notificationVisibility: NotificationVisibility) {
		storage[notificationVisibility.id] = notificationVisibility
	}

	fun clear() {
		storage.clear()
	}
}