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
import kr.kro.dokbaro.server.core.account.application.port.out.ExistEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import kr.kro.dokbaro.server.core.account.application.service.exception.AccountNotFoundException
import kr.kro.dokbaro.server.core.account.application.service.exception.AlreadyRegisteredEmailException
import kr.kro.dokbaro.server.core.account.application.service.exception.PasswordNotMatchException
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.emailAccountFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.springframework.security.crypto.password.NoOpPasswordEncoder

class EmailAccountServiceTest :
	StringSpec({
		val insertAccountPasswordPort = mockk<InsertAccountPasswordPort>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val passwordEncoder = NoOpPasswordEncoder.getInstance()
		val useAuthenticatedEmailUseCase = mockk<UseAuthenticatedEmailUseCase>()
		val temporaryPasswordGenerator = TemporaryPasswordGenerator()
		val loadEmailAccountPort = mockk<LoadEmailAccountPort>()
		val updateEmailAccountPort = UpdateEmailAccountPortMock()
		val sendTemporaryPasswordPort = mockk<SendTemporaryPasswordPort>()
		val existEmailAccountPort = mockk<ExistEmailAccountPort>()

		val emailAccountService =
			EmailAccountService(
				insertAccountPasswordPort,
				registerMemberUseCase,
				passwordEncoder,
				useAuthenticatedEmailUseCase,
				temporaryPasswordGenerator,
				loadEmailAccountPort,
				updateEmailAccountPort,
				sendTemporaryPasswordPort,
				existEmailAccountPort,
			)

		afterEach {
			clearAllMocks()
			updateEmailAccountPort.clear()
		}

		"email 계정 등록을 수행한다" {
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { insertAccountPasswordPort.insertEmailAccount(any()) } returns Unit
			every { existEmailAccountPort.existsByEmail(any()) } returns false

			val command =
				RegisterEmailAccountCommand(
					email = "example@example.com",
					nickname = "exampleNickname",
					password = "securePassword123",
					profileImage = "https://example.com/profile.jpg",
				)

			emailAccountService.registerEmailAccount(command)

			verify { insertAccountPasswordPort.insertEmailAccount(any()) }
		}

		"이미 존재하는 이메일 계정이 있으면 예외를 반환한다" {
			every { useAuthenticatedEmailUseCase.useEmail(any()) } returns Unit
			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { insertAccountPasswordPort.insertEmailAccount(any()) } returns Unit
			every { existEmailAccountPort.existsByEmail(any()) } returns true

			val command =
				RegisterEmailAccountCommand(
					email = "example@example.com",
					nickname = "exampleNickname",
					password = "securePassword123",
					profileImage = "https://example.com/profile.jpg",
				)

			shouldThrow<AlreadyRegisteredEmailException> {
				emailAccountService.registerEmailAccount(command)
			}
		}

		"임시 비밀번호를 생성한다" {
			every { loadEmailAccountPort.findByEmail(any()) } returns emailAccountFixture()
			every { sendTemporaryPasswordPort.sendTemporaryPassword(any(), any()) } returns Unit

			emailAccountService.issueTemporaryPassword("hello@ex.com")

			updateEmailAccountPort.storage.shouldNotBeEmpty()
			verify { sendTemporaryPasswordPort.sendTemporaryPassword(any(), any()) }
		}

		"임시 비밀번호 생성 혹은 비밀번호 변경 시 email에 해당하는 계정이 없으면 예외를 반환한다" {
			every { loadEmailAccountPort.findByEmail(any()) } returns null
			every { loadEmailAccountPort.findByMemberId(any()) } returns null

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
			every { loadEmailAccountPort.findByMemberId(any()) } returns
				emailAccountFixture(
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
				updateEmailAccountPort.storage.first().password,
			) shouldBe true
		}

		"비밀번호 변경 시 기존 비밀번호가 일치하지 않으면 예외를 반환한다" {
			val oldPassword = "old"
			val newPassword = "new"
			every { loadEmailAccountPort.findByMemberId(any()) } returns
				emailAccountFixture(
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