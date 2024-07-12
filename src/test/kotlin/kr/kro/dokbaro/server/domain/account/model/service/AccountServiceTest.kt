package kr.kro.dokbaro.server.domain.account.model.service

import io.kotest.core.spec.style.StringSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.domain.account.port.output.ExistAccountPort
import kr.kro.dokbaro.server.domain.account.port.output.SaveAccountPort
import java.time.Clock

class AccountServiceTest :
	StringSpec({
		val existAccountPort = mockk<ExistAccountPort>()
		val saveAccountPort = mockk<SaveAccountPort>()
		val clock = Clock.systemUTC()

		val accountService = AccountService(existAccountPort, saveAccountPort, clock)

		val command = RegisterAccountCommand("socialId", "GOOGLE")

		afterEach {
			clearAllMocks()
		}

		"신규 회원이면 등록을 진행한다" {
			every { existAccountPort.notExistBy(command.socialId) } returns true
			every { saveAccountPort.save(any()) } returns 5

			accountService.register(command)

			verify(exactly = 1) { saveAccountPort.save(any()) }
		}

		"이미 있는 회원이면 등록을 진행하지 않는다" {
			every { existAccountPort.notExistBy(command.socialId) } returns false

			accountService.register(command)

			verify(exactly = 0) { saveAccountPort.save(any()) }
		}
	})