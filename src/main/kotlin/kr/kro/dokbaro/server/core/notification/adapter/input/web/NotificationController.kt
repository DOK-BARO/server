package kr.kro.dokbaro.server.core.notification.adapter.input.web

import kr.kro.dokbaro.server.core.notification.application.port.input.CheckAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.DisableNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.FindAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notifications")
class NotificationController(
	private val checkAllNotificationUseCase: CheckAllNotificationUseCase,
	private val disableNotificationUseCase: DisableNotificationUseCase,
	private val findAllNotificationUseCase: FindAllNotificationUseCase,
) {
	@PostMapping("/check")
	fun checkAll(
		@Login user: DokbaroUser,
	) {
		checkAllNotificationUseCase.checkAll(memberId = user.id)
	}

	@PostMapping("/{id}/disable")
	fun disableNotification(
		@PathVariable id: Long,
		@Login user: DokbaroUser,
	) {
		disableNotificationUseCase.disableBy(
			notificationId = id,
			memberId = user.id,
		)
	}

	@GetMapping
	fun findAll(
		@Login user: DokbaroUser,
	): Collection<NotificationResult> = findAllNotificationUseCase.findAllBy(memberId = user.id)
}