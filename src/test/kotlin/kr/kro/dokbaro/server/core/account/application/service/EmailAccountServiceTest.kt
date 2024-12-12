package kr.kro.dokbaro.server.core.account.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import kr.kro.dokbaro.server.core.account.application.service.exception.AccountNotFoundException
import kr.kro.dokbaro.server.core.account.application.service.exception.PasswordNotMatchException
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.accountPasswordFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.springframework.security.crypto.password.NoOpPasswordEncoder

class EmailAccountServiceTest :
	StringSpec({
		val insertAccountPasswordPort = mockk<InsertAccountPasswordPort>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val passwordEncoder = NoOpPasswordEncoder.getInstance()
		val useAuthenticatedEmailUseCase = mockk<UseAuthenticatedEmailUseCase>()
		val temporaryPasswordGenerator = TemporaryPasswordGenerator()
		val loadAccountPasswordPort = mockk<LoadAccountPasswordPort>()
		val updateAccountPasswordPort = UpdateAccountPasswordPortMock()
		val sendTemporaryPasswordPort = mockk<SendTemporaryPasswordPort>()

		val emailAccountService =
			EmailAccountService(
				insertAccountPasswordPort,
				registerMemberUseCase,
				passwordEncoder,
				useAuthenticatedEmailUseCase,
				temporaryPasswordGenerator,
				loadAccountPasswordPort,
				updateAccountPasswordPort,
				sendTemporaryPasswordPort,
			)

		afterEach {
			clearAllMocks()
			updateAccountPasswordPort.clear()
		}

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

		"임시 비밀번호를 생성한다" {
			every { loadAccountPasswordPort.findByEmail(any()) } returns accountPasswordFixture()
			every { sendTemporaryPasswordPort.sendTemporaryPassword(any(), any()) } returns Unit

			emailAccountService.issueTemporaryPassword("hello@ex.com")

			updateAccountPasswordPort.storage.shouldNotBeEmpty()
			verify { sendTemporaryPasswordPort.sendTemporaryPassword(any(), any()) }
		}

		"임시 비밀번호 생성 혹은 비밀번호 변경 시 email에 해당하는 계정이 없으면 예외를 반환한다" {
			every { loadAccountPasswordPort.findByEmail(any()) } returns null
			every { loadAccountPasswordPort.findByMemberId(any()) } returns null

			shouldThrow<AccountNotFoundException> {
				emailAccountService.issueTemporaryPassword("hello@ex.com")
			}

			shouldThrow<AccountNotFoundException> {
				emailAccountService.changePassword(
					ChangePasswordCommand(
						1,
						"old",
						"new",
					),
				)
			}
		}

		"password를 변경한다" {
			val oldPassword = "old"
			val newPassword = "new"
			every { loadAccountPasswordPort.findByMemberId(any()) } returns
				accountPasswordFixture(
					password = passwordEncoder.encode(oldPassword),
				)
			emailAccountService.changePassword(
				ChangePasswordCommand(
					1,
					oldPassword,
					newPassword,
				),
			)

			passwordEncoder.matches(
				newPassword,
				updateAccountPasswordPort.storage.first().password,
			) shouldBe true
		}

		"비밀번호 변경 시 기존 비밀번호가 일치하지 않으면 예외를 반환한다" {
			val oldPassword = "old"
			val newPassword = "new"
			every { loadAccountPasswordPort.findByMemberId(any()) } returns
				accountPasswordFixture(
					password = passwordEncoder.encode(oldPassword),
				)

			shouldThrow<PasswordNotMatchException> {
				emailAccountService.changePassword(
					ChangePasswordCommand(
						1,
						"wrong",
						newPassword,
					),
				)
			}
		}
	})