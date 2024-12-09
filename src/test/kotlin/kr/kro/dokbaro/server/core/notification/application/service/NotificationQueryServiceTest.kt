package kr.kro.dokbaro.server.core.notification.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.notification.application.port.out.ReadNotificationResultCollectionPort
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import java.time.LocalDateTime

class NotificationQueryServiceTest :
	StringSpec({
		val readNotificationResultCollectionPort = mockk<ReadNotificationResultCollectionPort>()

		val notificationQueryService =
			NotificationQueryService(readNotificationResultCollectionPort)

		"member의 알림 목록을 조회한다" {

			every { readNotificationResultCollectionPort.findAllBy(any()) } returns
				listOf(
					NotificationResult(
						content = "새로운 알림 내용입니다.",
						trigger = NotificationTrigger.UPDATE_QUIZ,
						imageUrl = "http://example.com/image.png",
						linkedId = 1234,
						checked = false,
						createdAt = LocalDateTime.now(),
					),
				)

			notificationQueryService.findAllBy(1) shouldNotBe null
		}
	})