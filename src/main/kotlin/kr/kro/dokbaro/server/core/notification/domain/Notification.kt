package kr.kro.dokbaro.server.core.notification.domain

import kr.kro.dokbaro.server.common.constant.Constants
import java.time.LocalDateTime

data class Notification(
	val content: String,
	val trigger: NotificationTrigger,
	val imageUrl: String? = null,
	val linkedId: Long? = null,
	val id: Long = Constants.UNSAVED_ID,
	val createdAt: LocalDateTime = LocalDateTime.now(),
)