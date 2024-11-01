package kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.entity.jooq.NotificationMapper
import kr.kro.dokbaro.server.core.notification.application.port.out.dto.LoadNotificationVisibilityCondition
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import org.jooq.DSLContext
import org.jooq.InsertValuesStep2
import org.jooq.Result
import org.jooq.generated.tables.JNotificationVisibility
import org.jooq.generated.tables.records.NotificationVisibilityRecord
import org.springframework.stereotype.Repository

@Repository
class NotificationVisibilityRepository(
	private val dslContext: DSLContext,
	private val notificationMapper: NotificationMapper,
) {
	companion object {
		private val NOTIFICATION_VISIBILITY = JNotificationVisibility.NOTIFICATION_VISIBILITY
	}

	fun insertAll(notificationVisibilities: Collection<NotificationVisibility>) {
		val insertQuery: Collection<InsertValuesStep2<NotificationVisibilityRecord, Long, Long>> =
			notificationVisibilities.map {
				dslContext
					.insertInto(
						NOTIFICATION_VISIBILITY,
						NOTIFICATION_VISIBILITY.NOTIFICATION_ID,
						NOTIFICATION_VISIBILITY.MEMBER_ID,
					).values(
						it.notificationId,
						it.memberId,
					)
			}

		dslContext.batch(insertQuery).execute()
	}

	fun findAllBy(condition: LoadNotificationVisibilityCondition): Collection<NotificationVisibility> {
		val record: Result<NotificationVisibilityRecord> =
			dslContext
				.select(
					NOTIFICATION_VISIBILITY.NOTIFICATION_ID,
					NOTIFICATION_VISIBILITY.MEMBER_ID,
					NOTIFICATION_VISIBILITY.CHECKED,
					NOTIFICATION_VISIBILITY.DISABLED,
					NOTIFICATION_VISIBILITY.ID,
				).from(
					NOTIFICATION_VISIBILITY,
				).where(
					NOTIFICATION_VISIBILITY.MEMBER_ID
						.eq(condition.memberId)
						.and(NOTIFICATION_VISIBILITY.CHECKED.eq(condition.checked))
						.and(NOTIFICATION_VISIBILITY.DISABLED.eq(condition.disabled)),
				).fetchInto(NOTIFICATION_VISIBILITY)

		return notificationMapper.toNotificationVisibilityCollection(record)
	}

	fun findBy(
		notificationId: Long,
		memberId: Long,
	): NotificationVisibility? {
		val record: Result<NotificationVisibilityRecord> =
			dslContext
				.select(
					NOTIFICATION_VISIBILITY.NOTIFICATION_ID,
					NOTIFICATION_VISIBILITY.MEMBER_ID,
					NOTIFICATION_VISIBILITY.CHECKED,
					NOTIFICATION_VISIBILITY.DISABLED,
					NOTIFICATION_VISIBILITY.ID,
				).from(
					NOTIFICATION_VISIBILITY,
				).where(
					NOTIFICATION_VISIBILITY.MEMBER_ID
						.eq(memberId)
						.and(NOTIFICATION_VISIBILITY.NOTIFICATION_ID.eq(notificationId)),
				).fetchInto(NOTIFICATION_VISIBILITY)

		return notificationMapper.toNotificationVisibility(record)
	}

	fun update(notificationVisibility: NotificationVisibility) {
		dslContext
			.update(NOTIFICATION_VISIBILITY)
			.set(NOTIFICATION_VISIBILITY.CHECKED, notificationVisibility.checked)
			.set(NOTIFICATION_VISIBILITY.DISABLED, notificationVisibility.disabled)
			.where(NOTIFICATION_VISIBILITY.ID.eq(notificationVisibility.id))
			.execute()
	}
}