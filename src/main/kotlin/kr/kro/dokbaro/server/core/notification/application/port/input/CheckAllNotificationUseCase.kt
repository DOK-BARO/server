package kr.kro.dokbaro.server.core.notification.application.port.input

fun interface CheckAllNotificationUseCase {
	fun checkAll(loginUserId: Long)
}