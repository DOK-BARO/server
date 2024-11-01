package kr.kro.dokbaro.server.core.notification.application.port.out

import kr.kro.dokbaro.server.core.notification.query.NotificationResult

fun interface ReadNotificationResultCollectionPort {
	fun findAllBy(memberId: Long): Collection<NotificationResult>
}