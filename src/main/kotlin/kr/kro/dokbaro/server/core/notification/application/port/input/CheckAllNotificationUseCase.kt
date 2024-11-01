package kr.kro.dokbaro.server.core.notification.application.port.input

import java.util.UUID

interface CheckAllNotificationUseCase {
	fun checkAll(authId: UUID)
}