package kr.kro.dokbaro.server.core.notification.adapter.input.web

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.notification.application.port.input.CheckAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.DisableNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.FindAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import org.springframework.security.core.Authentication
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
	fun checkAll(auth: Authentication) {
		checkAllNotificationUseCase.checkAll(UUIDUtils.stringToUUID(auth.name))
	}

	@PostMapping("/{id}/disable")
	fun disableNotification(
		@PathVariable id: Long,
		auth: Authentication,
	) {
		disableNotificationUseCase.disableBy(id, UUIDUtils.stringToUUID(auth.name))
	}

	@GetMapping
	fun findAll(auth: Authentication): Collection<NotificationResult> =
		findAllNotificationUseCase.findAllBy(UUIDUtils.stringToUUID(auth.name))
}