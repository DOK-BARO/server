package kr.kro.dokbaro.server.core.notification.application.port.out.dto

data class LoadNotificationVisibilityCondition(
	val memberId: Long,
	val checked: Boolean,
	val disabled: Boolean,
)