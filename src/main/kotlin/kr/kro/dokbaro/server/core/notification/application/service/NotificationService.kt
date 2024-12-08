package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.input.CheckAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.DisableNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityCollectionPort
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.port.out.UpdateNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.port.out.dto.LoadNotificationVisibilityCondition
import kr.kro.dokbaro.server.core.notification.application.service.exception.NotFountNotificationVisibilityException
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificationService(
	private val loadNotificationVisibilityCollectionPort: LoadNotificationVisibilityCollectionPort,
	private val loadNotificationVisibilityPort: LoadNotificationVisibilityPort,
	private val updateNotificationVisibilityPort: UpdateNotificationVisibilityPort,
) : CheckAllNotificationUseCase,
	DisableNotificationUseCase {
	override fun checkAll(authId: UUID) {
		val loginMember = TODO()

		val unCheckedNotificationVisibility: Collection<NotificationVisibility> =
			loadNotificationVisibilityCollectionPort.findAllBy(
				LoadNotificationVisibilityCondition(
					memberId = TODO(),
					checked = false,
					disabled = false,
				),
			)

		unCheckedNotificationVisibility.forEach {
			it.check()
			updateNotificationVisibilityPort.update(it)
		}
	}

	override fun disableBy(
		notificationId: Long,
		authId: UUID,
	) {
		val loginMember = TODO()

		val notificationVisibility: NotificationVisibility =
			loadNotificationVisibilityPort.findBy(notificationId, TODO())
				?: throw NotFountNotificationVisibilityException()

		notificationVisibility.disable()

		updateNotificationVisibilityPort.update(notificationVisibility)
	}
}