package kr.kro.dokbaro.server.core.auth.email.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import kr.kro.dokbaro.server.core.auth.email.application.port.out.SaveEmailAccountPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import java.util.UUID

class EmailSignUpServiceTest :
	StringSpec({
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val useAuthenticatedEmailUseCase = mockk<UseAuthenticatedEmailUseCase>()
		val passwordEncoder = NoOpPasswordEncoder.getInstance()
		val saveEmailAccountPort = mockk<SaveEmailAccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val emailSignUpService =
			EmailSignUpService(
				registerMemberUseCase,
				useAuthenticatedEmailUseCase,
				passwordEncoder,
				saveEmailAccountPort,
				generateAuthTokenUseCase,
			)

		"email을 통한 회원가입을 수행한다" {
			val email = "www@email.com"
			val certificationId = UUID.randomUUID()
			val role = setOf(Role.GUEST)
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			every { registerMemberUseCase.register(any()) } returns
				Member(
					"nick",
					Email(email),
					"profile.png",
					certificationId,
					role,
					4,
				)
			every { saveEmailAccountPort.save(any()) } returns 1L
			val authToken = AuthToken("access", "refresh")
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