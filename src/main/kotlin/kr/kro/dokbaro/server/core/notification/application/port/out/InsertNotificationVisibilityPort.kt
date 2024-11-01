package kr.kro.dokbaro.server.core.notification.application.port.out

import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

fun interface InsertNotificationVisibilityPort {
	fun insertAll(notificationVisibilities: Collection<NotificationVisibility>)
}