package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationQueryRepository
import kr.kro.dokbaro.server.core.notification.application.port.out.ReadNotificationResultCollectionPort
import kr.kro.dokbaro.server.core.notification.query.NotificationResult

@PersistenceAdapter
class NotificationPersistenceQueryAdapter(
	private val notificationQueryRepository: NotificationQueryRepository,
) : ReadNotificationResultCollectionPort {
	override fun findAllBy(memberId: Long): Collection<NotificationResult> =
		notificationQueryRepository.findAllBy(memberId)
}