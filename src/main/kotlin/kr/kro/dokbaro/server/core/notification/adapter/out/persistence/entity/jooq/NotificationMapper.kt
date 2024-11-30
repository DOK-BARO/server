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
				id = it[NOTIFICATION_VISIBILITY.ID],
				notificationId = it[NOTIFICATION_VISIBILITY.NOTIFICATION_ID],
				memberId = it[NOTIFICATION_VISIBILITY.MEMBER_ID],
				checked = it[NOTIFICATION_VISIBILITY.CHECKED],
				disabled = it[NOTIFICATION_VISIBILITY.DISABLED],
			)
		}

	fun toNotificationVisibility(record: Result<NotificationVisibilityRecord>): NotificationVisibility? =
		record
			.map {
				NotificationVisibility(
					id = it[NOTIFICATION_VISIBILITY.ID],
					notificationId = it[NOTIFICATION_VISIBILITY.NOTIFICATION_ID],
					memberId = it[NOTIFICATION_VISIBILITY.MEMBER_ID],
					checked = it[NOTIFICATION_VISIBILITY.CHECKED],
					disabled = it[NOTIFICATION_VISIBILITY.DISABLED],
				)
			}.firstOrNull()

	fun toNotificationResult(record: Result<out Record>): Collection<NotificationResult> =
		record.map {
			NotificationResult(
				id = it[NOTIFICATION.ID],
				content = it[NOTIFICATION.CONTENT],
				trigger = NotificationTrigger.valueOf(it[NOTIFICATION.NOTIFICATION_TRIGGER]),
				imageUrl = it[NOTIFICATION.IMAGE_URL],
				linkedId = it[NOTIFICATION.LINKED_ID],
				checked = it[NOTIFICATION_VISIBILITY.CHECKED],
				createdAt = it[NOTIFICATION.CREATED_AT],
			)
		}
}