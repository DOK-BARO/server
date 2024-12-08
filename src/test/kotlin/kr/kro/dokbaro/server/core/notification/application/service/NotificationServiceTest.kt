package kr.kro.dokbaro.server.core.notification.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityCollectionPort
import kr.kro.dokbaro.server.core.notification.application.port.out.LoadNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.application.service.exception.NotFountNotificationVisibilityException
import kr.kro.dokbaro.server.fixture.domain.notificationVisibilityFixture
import java.util.UUID

class NotificationServiceTest :
	StringSpec({
		val loadNotificationVisibilityCollectionPort = mockk<LoadNotificationVisibilityCollectionPort>()
		val loadNotificationVisibilityPort = mockk<LoadNotificationVisibilityPort>()
		val updateNotificationVisibilityPort = UpdateNotificationVisibilityPortMock()

		val notificationService =
			NotificationService(
				loadNotificationVisibilityCollectionPort,
				loadNotificationVisibilityPort,
				updateNotificationVisibilityPort,
			)

		afterEach {
			clearAllMocks()
			updateNotificationVisibilityPort.clear()
		}

		"유저 알림을 전체 체크를 진행한다" {
			every { loadNotificationVisibilityCollectionPort.findAllBy(any()) } returns
				listOf(
					notificationVisibilityFixture(id = 1),
					notificationVisibilityFixture(id = 2),
					notificationVisibilityFixture(id = 3),
				)

			notificationService.checkAll(UUID.randomUUID())

			updateNotificationVisibilityPort.storage.size shouldBe 3
			updateNotificationVisibilityPort.storage.values
				.map { it.checked }
				.shouldNotBeIn(false)
		}

		"유저 알림에 비활성화를 진행한다" {
			every { loadNotificationVisibilityPort.findBy(any(), any()) } returns notificationVisibilityFixture(id = 1)

			notificationService.disableBy(1, UUID.randomUUID())

			updateNotificationVisibilityPort.storage.size shouldBe 1
			updateNotificationVisibilityPort.storage[1]!!.disabled shouldBe true
		}

		"유저 알림 비활성화 진행 시 찾는 알림 ID에 해당하는 알림이 없다면 예외를 발생한다" {
			every { loadNotificationVisibilityPort.findBy(any(), any()) } returns null

			shouldThrow<NotFountNotificationVisibilityException> {
				notificationService.disableBy(1, UUID.randomUUID())
			}
		}
	})