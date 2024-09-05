package kr.kro.dokbaro.server.core.auth.email.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailLoginCommand
import kr.kro.dokbaro.server.core.auth.email.application.port.out.LoadEmailCertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

class EmailLoginServiceTest :
	StringSpec({
		val loadEmailCertificatedAccountPort = mockk<LoadEmailCertificatedAccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()
		val passwordEncoder = mockk<PasswordEncoder>()

		val emailLoginService = EmailLoginService(loadEmailCertificatedAccountPort, generateAuthTokenUseCase, passwordEncoder)

		"email을 통한 로그인을 수행한다" {
			every { loadEmailCertificatedAccountPort.findByEmail(any()) } returns
				EmailCertificatedAccount("password", UUID.randomUUID(), setOf("ADMIN"))
			every { passwordEncoder.matches(any(), any()) } returns true
			val authToken = AuthToken("access", "refresh")

			every { generateAuthTokenUseCase.generate(any()) } returns authToken

			emailLoginService.login(
				EmailLoginCommand(
					"www@example.com",
					"password",
				),
			) shouldBe authToken
		}

		"email에 해당하는 계정이 없으면 예외를 반환한다" {
			every { loadEmailCertificatedAccountPort.findByEmail(any()) } returns null

			shouldThrow<NotFoundEmailCertificatedAccountException> {
				emailLoginService.login(
					EmailLoginCommand(
						"www@example.com",
						"password",
					),
				)
			}
		}

		"비밀번호가 일치하지 않으면 예외를 반환한다" {
			every { loadEmailCertificatedAccountPort.findByEmail(any()) } returns
				EmailCertificatedAccount("password", UUID.randomUUID(), setOf("ADMIN"))
			every { passwordEncoder.matches(any(), any()) } returns false

			shouldThrow<PasswordNotMatchException> {
				emailLoginService.login(
					EmailLoginCommand(
						"www@example.com",
						"password",
					),
				)
			}
		}
	})