package kr.kro.dokbaro.server.core.account.application.service

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.springframework.security.crypto.password.NoOpPasswordEncoder

class EmailAccountServiceTest :
	StringSpec({
		val insertAccountPasswordPort = mockk<InsertAccountPasswordPort>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val passwordEncoder = NoOpPasswordEncoder.getInstance()
		val useAuthenticatedEmailUseCase = mockk<UseAuthenticatedEmailUseCase>()

		val emailAccountService =
			EmailAccountService(insertAccountPasswordPort, registerMemberUseCase, passwordEncoder, useAuthenticatedEmailUseCase)

		"email 계정 등록을 수행한다" {
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { insertAccountPasswordPort.insertAccountPassword(any()) } returns Unit

			val command =
				RegisterEmailAccountCommand(
					email = "example@example.com",
					nickname = "exampleNickname",
					password = "securePassword123",
					profileImage = "https://example.com/profile.jpg",
				)

			emailAccountService.registerEmailAccount(command)

			verify { insertAccountPasswordPort.insertAccountPassword(any()) }
		}
	})