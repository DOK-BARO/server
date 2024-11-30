package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.notification.domain.Notification
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import java.time.LocalDateTime

fun notificationFixture(
	id: Long = Constants.UNSAVED_ID,
	content: String = "hello world",
	trigger: NotificationTrigger = NotificationTrigger.UPDATE_QUIZ,
	imageUrl: String? = null,
	linkedId: Long? = null,
	createdAt: LocalDateTime = LocalDateTime.now(),
): Notification =
	Notification(
		id = id,
		content = content,
		trigger = trigger,
		imageUrl = imageUrl,
		linkedId = linkedId,
		createdAt = createdAt,
	)

fun notificationVisibilityFixture(
	id: Long = Constants.UNSAVED_ID,
	notificationId: Long = Constants.UNSAVED_ID,
	memberId: Long = Constants.UNSAVED_ID,
	checked: Boolean = false,
	disabled: Boolean = false,
): NotificationVisibility =
	NotificationVisibility(
		id = id,
		notificationId = notificationId,
		memberId = memberId,
		checked = checked,
		disabled = disabled,
	)