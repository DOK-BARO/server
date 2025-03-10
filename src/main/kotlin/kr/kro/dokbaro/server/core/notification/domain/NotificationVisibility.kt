package kr.kro.dokbaro.server.core.notification.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class NotificationVisibility(
	val id: Long = Constants.UNSAVED_ID,
	val notificationId: Long,
	val memberId: Long,
	var checked: Boolean = false,
	var disabled: Boolean = false,
) {
	fun check() {
		checked = true
	}

	fun disable() {
		disabled = true
	}
}