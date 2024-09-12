package kr.kro.dokbaro.server.core.auth.email.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import kr.kro.dokbaro.server.core.auth.email.application.port.out.InsertEmailAccountPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.springframework.security.crypto.password.NoOpPasswordEncoder

class EmailSignUpServiceTest :
	StringSpec({
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val useAuthenticatedEmailUseCase = mockk<UseAuthenticatedEmailUseCase>()
		val passwordEncoder = NoOpPasswordEncoder.getInstance()
		val insertEmailAccountPort = mockk<InsertEmailAccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val emailSignUpService =
			EmailSignUpService(
				registerMemberUseCase,
				useAuthenticatedEmailUseCase,
				passwordEncoder,
				insertEmailAccountPort,
				generateAuthTokenUseCase,
			)

		"email을 통한 회원가입을 수행한다" {
			val email = "www@email.com"
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { insertEmailAccountPort.insert(any()) } returns 1L
			val authToken = authTokenFixture()
			every { generateAuthTokenUseCase.generate(any()) } returns authToken

			emailSignUpService.signUp(
				EmailSignUpCommand(
					email,
					"password",
					"nick",
					"profile.png",
				),
			) shouldBe authToken
		}
	})