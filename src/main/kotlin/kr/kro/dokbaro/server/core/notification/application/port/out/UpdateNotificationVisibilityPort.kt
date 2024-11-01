package kr.kro.dokbaro.server.core.notification.application.port.out

import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

interface UpdateNotificationVisibilityPort {
	fun update(notificationVisibility: NotificationVisibility)
}