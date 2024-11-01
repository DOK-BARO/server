package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationVisibilityRepository
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityCollectionPort
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.port.out.UpdateNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.port.out.dto.LoadNotificationVisibilityCondition
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

@PersistenceAdapter
class NotificationVisibilityPersistenceAdapter(
	private val notificationVisibilityRepository: NotificationVisibilityRepository,
) : InsertNotificationVisibilityPort,
	LoadNotificationVisibilityCollectionPort,
	LoadNotificationVisibilityPort,
	UpdateNotificationVisibilityPort {
	override fun insertAll(notificationVisibilities: Collection<NotificationVisibility>) =
		notificationVisibilityRepository.insertAll(notificationVisibilities)

	override fun findAllBy(condition: LoadNotificationVisibilityCondition): Collection<NotificationVisibility> =
		notificationVisibilityRepository.findAllBy(condition)

	override fun findBy(
		notificationId: Long,
		memberId: Long,
	): NotificationVisibility? = notificationVisibilityRepository.findBy(notificationId, memberId)

	override fun update(notificationVisibility: NotificationVisibility) =
		notificationVisibilityRepository.update(notificationVisibility)
}