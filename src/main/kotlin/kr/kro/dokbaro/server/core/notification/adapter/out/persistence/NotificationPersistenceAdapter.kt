package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationRepository
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationPort
import kr.kro.dokbaro.server.core.notification.domain.Notification

@PersistenceAdapter
class NotificationPersistenceAdapter(
	private val notificationRepository: NotificationRepository,
) : InsertNotificationPort {
	override fun insert(notification: Notification): Long = notificationRepository.insert(notification)
}