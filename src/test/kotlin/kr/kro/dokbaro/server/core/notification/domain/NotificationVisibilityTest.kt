package kr.kro.dokbaro.server.core.notification.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.fixture.domain.notificationVisibilityFixture

class NotificationVisibilityTest :
	StringSpec({
		"체크를 수행한다" {
			val notificationVisibility = notificationVisibilityFixture()

			notificationVisibility.check()

			notificationVisibility.checked shouldBe true
		}

		"비활성화를 수행한다" {
			val notificationVisibility = notificationVisibilityFixture()

			notificationVisibility.disable()

			notificationVisibility.disabled shouldBe true
		}
	})