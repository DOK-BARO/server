package kr.kro.dokbaro.server.core.notification.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.notification.application.port.out.ReadNotificationResultCollectionPort
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import java.time.LocalDateTime
import java.util.UUID

class NotificationQueryServiceTest :
	StringSpec({
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val readNotificationResultCollectionPort = mockk<ReadNotificationResultCollectionPort>()

		val notificationQueryService =
			NotificationQueryService(findCertificatedMemberUseCase, readNotificationResultCollectionPort)

		"member의 알림 목록을 조회한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
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

			notificationQueryService.findAllBy(UUID.randomUUID()) shouldNotBe null
		}
	})