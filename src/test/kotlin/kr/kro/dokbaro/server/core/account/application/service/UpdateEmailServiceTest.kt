package kr.kro.dokbaro.server.core.account.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.account.application.port.out.LoadEmailAccountPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.fixture.domain.emailAccountFixture

class UpdateEmailServiceTest :
	StringSpec({

		val loadEmailAccountPort: LoadEmailAccountPort = mockk()
		val updateEmailAccountPort = UpdateEmailAccountPortMock()
		val useAuthenticatedEmailUseCase: UseAuthenticatedEmailUseCase = mockk()

		val updateEmailService =
			UpdateEmailService(
				loadEmailAccountPort,
				updateEmailAccountPort,
				useAuthenticatedEmailUseCase,
			)

		afterEach {
			clearAllMocks()
			updateEmailAccountPort.clear()
		}

		"이메일을 수정한다" {
			every { loadEmailAccountPort.findByMemberId(any()) } returns emailAccountFixture()
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit

			val email = "hello@example.com"
			updateEmailService.updateEmail(1, email)

			updateEmailAccountPort.storage.first().email shouldBe email
		}

		"이메일 수정 시 해당하는 값이 없으면 업테이트를 진행하지 않는다" {
			every { loadEmailAccountPort.findByMemberId(any()) } returns null
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			val email = "hello@example.com"
			updateEmailService.updateEmail(1, email)

			updateEmailAccountPort.storage.shouldBeEmpty()
		}
	})