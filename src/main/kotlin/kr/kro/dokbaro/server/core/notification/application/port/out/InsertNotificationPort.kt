package kr.kro.dokbaro.server.core.notification.application.port.out

import kr.kro.dokbaro.server.core.notification.domain.Notification

fun interface InsertNotificationPort {
	fun insert(notification: Notification): Long
}