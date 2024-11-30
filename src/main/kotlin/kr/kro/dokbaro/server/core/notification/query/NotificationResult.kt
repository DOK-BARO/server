package kr.kro.dokbaro.server.core.notification.query

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import java.time.LocalDateTime

data class NotificationResult(
	val id: Long = Constants.UNSAVED_ID,
	val content: String,
	val trigger: NotificationTrigger,
	val imageUrl: String?,
	val linkedId: Long?,
	val checked: Boolean,
	val createdAt: LocalDateTime,
)