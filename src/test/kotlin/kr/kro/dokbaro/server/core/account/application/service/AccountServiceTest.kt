package kr.kro.dokbaro.server.core.account.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.output.ExistAccountPort
import kr.kro.dokbaro.server.core.account.application.port.output.SaveAccountPort
import java.time.Clock

class AccountServiceTest :
	StringSpec({
		val existAccountPort = mockk<ExistAccountPort>()
		val saveAccountPort = mockk<SaveAccountPort>()
		val clock = Clock.systemUTC()

		val accountService = AccountService(existAccountPort, saveAccountPort, clock)

		val command = RegisterAccountCommand("socialId", AuthProvider.GOOGLE)

		afterEach {
			clearAllMocks()
		}

		"신규 회원이면 등록을 진행한다" {
			every { existAccountPort.existBy(command.socialId, any(AuthProvider::class)) } returns false
			every { saveAccountPort.save(any()) } returns 5

			accountService.register(command)

			verify(exactly = 1) { saveAccountPort.save(any()) }
		}

		"이미 있는 회원이면 예외를 반환한다" {
			every { existAccountPort.existBy(command.socialId, any(AuthProvider::class)) } returns true

			shouldThrow<RuntimeException> {
				accountService.register(command)
			}
		}
	})