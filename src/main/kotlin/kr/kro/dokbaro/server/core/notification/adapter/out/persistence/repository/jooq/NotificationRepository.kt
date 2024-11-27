package kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.notification.domain.Notification
import org.jooq.DSLContext
import org.jooq.generated.tables.JNotification
import org.springframework.stereotype.Repository

@Repository
class NotificationRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val NOTIFICATION = JNotification.NOTIFICATION
	}

	fun insert(notification: Notification): Long =
		dslContext
			.insertInto(
				NOTIFICATION,
				NOTIFICATION.CONTENT,
				NOTIFICATION.NOTIFICATION_TRIGGER,
				NOTIFICATION.IMAGE_URL,
				NOTIFICATION.LINKED_ID,
			).values(
				notification.content,
				notification.trigger.name,
				notification.imageUrl,
				notification.linkedId,
			).returningResult(NOTIFICATION.ID)
			.fetchOneInto(Long::class.java)!!
}