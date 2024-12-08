package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.input.FindAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.out.ReadNotificationResultCollectionPort
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificationQueryService(
	private val readNotificationResultCollectionPort: ReadNotificationResultCollectionPort,
) : FindAllNotificationUseCase {
	override fun findAllBy(authId: UUID): Collection<NotificationResult> {
		val loginMember = TODO()

		return readNotificationResultCollectionPort.findAllBy(memberId = TODO())
	}
}