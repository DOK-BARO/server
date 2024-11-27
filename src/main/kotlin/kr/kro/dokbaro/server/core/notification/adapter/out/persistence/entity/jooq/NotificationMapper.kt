package kr.kro.dokbaro.server.core.notification.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JNotification
import org.jooq.generated.tables.JNotificationVisibility
import org.jooq.generated.tables.records.NotificationVisibilityRecord

@Mapper
class NotificationMapper {
	companion object {
		private val NOTIFICATION = JNotification.NOTIFICATION
		private val NOTIFICATION_VISIBILITY = JNotificationVisibility.NOTIFICATION_VISIBILITY
	}

	fun toNotificationVisibilityCollection(
		record: Result<NotificationVisibilityRecord>,
	): Collection<NotificationVisibility> =
		record.map {
			NotificationVisibility(
				it.get(NOTIFICATION_VISIBILITY.NOTIFICATION_ID),
				it.get(NOTIFICATION_VISIBILITY.MEMBER_ID),
				it.get(NOTIFICATION_VISIBILITY.CHECKED),
				it.get(NOTIFICATION_VISIBILITY.DISABLED),
				it.get(NOTIFICATION_VISIBILITY.ID),
			)
		}

	fun toNotificationVisibility(record: Result<NotificationVisibilityRecord>): NotificationVisibility? =
		record
			.map {
				NotificationVisibility(
					it.get(NOTIFICATION_VISIBILITY.NOTIFICATION_ID),
					it.get(NOTIFICATION_VISIBILITY.MEMBER_ID),
					it.get(NOTIFICATION_VISIBILITY.CHECKED),
					it.get(NOTIFICATION_VISIBILITY.DISABLED),
					it.get(NOTIFICATION_VISIBILITY.ID),
				)
			}.firstOrNull()

	fun toNotificationResult(record: Result<out Record>): Collection<NotificationResult> =
		record.map {
			NotificationResult(
				it.get(NOTIFICATION.CONTENT),
				NotificationTrigger.valueOf(it.get(NOTIFICATION.NOTIFICATION_TRIGGER)),
				it.get(NOTIFICATION.IMAGE_URL),
				it.get(NOTIFICATION.LINKED_ID),
				it.get(NOTIFICATION_VISIBILITY.CHECKED),
				it.get(NOTIFICATION.CREATED_AT),
				it.get(NOTIFICATION.ID),
			)
		}
}