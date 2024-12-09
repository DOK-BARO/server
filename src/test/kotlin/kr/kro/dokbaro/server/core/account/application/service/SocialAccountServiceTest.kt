package kr.kro.dokbaro.server.core.account.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterSocialAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertSocialAccountPort
import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.memberFixture

class SocialAccountServiceTest :
	StringSpec({
		val insertSocialAccountPort = mockk<InsertSocialAccountPort>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()

		val socialAccountService = SocialAccountService(insertSocialAccountPort, registerMemberUseCase)

		"소셜 계정을 등록한다" {
			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { insertSocialAccountPort.insertSocialAccount(any()) } returns Unit

			val command =
				RegisterSocialAccountCommand(
					socialId = "1234567890",
					email = "socialuser@example.com",
					nickname = "SocialUser",
					provider = AuthProvider.GOOGLE,
					profileImage = "https://example.com/social-profile.jpg",
				)

			socialAccountService.registerSocialAccount(command) shouldNotBe null
		}
	})