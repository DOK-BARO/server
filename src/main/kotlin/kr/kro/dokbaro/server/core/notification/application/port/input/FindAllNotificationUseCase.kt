package kr.kro.dokbaro.server.core.notification.application.port.input

import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import java.util.UUID

fun interface FindAllNotificationUseCase {
	fun findAllBy(authId: UUID): Collection<NotificationResult>
}