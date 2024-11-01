package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.notification.domain.Notification
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import java.time.LocalDateTime

fun notificationFixture(
	content: String = "hello world",
	trigger: NotificationTrigger = NotificationTrigger.UPDATE_QUIZ,
	imageUrl: String? = null,
	linkedId: Long? = null,
	id: Long = Constants.UNSAVED_ID,
	createdAt: LocalDateTime = LocalDateTime.now(),
): Notification =
	Notification(
		content = content,
		trigger = trigger,
		imageUrl = imageUrl,
		linkedId = linkedId,
		id = id,
		createdAt = createdAt,
	)

fun notificationVisibilityFixture(
	notificationId: Long = Constants.UNSAVED_ID,
	memberId: Long = Constants.UNSAVED_ID,
	checked: Boolean = false,
	disabled: Boolean = false,
	id: Long = Constants.UNSAVED_ID,
): NotificationVisibility =
	NotificationVisibility(
		notificationId = notificationId,
		memberId = memberId,
		checked = checked,
		disabled = disabled,
		id = id,
	)