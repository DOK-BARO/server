package kr.kro.dokbaro.server.core.notification.application.port.out

import kr.kro.dokbaro.server.core.notification.application.port.out.dto.LoadNotificationVisibilityCondition
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility

interface LoadNotificationVisibilityCollectionPort {
	fun findAllBy(condition: LoadNotificationVisibilityCondition): Collection<NotificationVisibility>
}