package kr.kro.dokbaro.server.core.notification.application.port.input

import java.util.UUID

fun interface CheckAllNotificationUseCase {
	fun checkAll(authId: UUID)
}