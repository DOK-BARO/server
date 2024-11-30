package kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.entity.jooq.NotificationMapper
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JNotification
import org.jooq.generated.tables.JNotificationVisibility
import org.springframework.stereotype.Repository

@Repository
class NotificationQueryRepository(
	private val dslContext: DSLContext,
	private val notificationMapper: NotificationMapper,
) {
	companion object {
		private val NOTIFICATION = JNotification.NOTIFICATION
		private val NOTIFICATION_VISIBILITY = JNotificationVisibility.NOTIFICATION_VISIBILITY
	}

	fun findAllBy(memberId: Long): Collection<NotificationResult> {
		val record: Result<out Record> =
			dslContext
				.select(
					NOTIFICATION.ID,
					NOTIFICATION.CONTENT,
					NOTIFICATION.IMAGE_URL,
					NOTIFICATION.LINKED_ID,
					NOTIFICATION.CREATED_AT,
					NOTIFICATION.NOTIFICATION_TRIGGER,
					NOTIFICATION_VISIBILITY.CHECKED,
				).from(NOTIFICATION_VISIBILITY)
				.join(NOTIFICATION)
				.on(NOTIFICATION.ID.eq(NOTIFICATION_VISIBILITY.NOTIFICATION_ID))
				.where(
					NOTIFICATION_VISIBILITY.MEMBER_ID
						.eq(memberId)
						.and(NOTIFICATION_VISIBILITY.DISABLED.isFalse),
				).fetch()

		return notificationMapper.toNotificationResult(record)
	}
}