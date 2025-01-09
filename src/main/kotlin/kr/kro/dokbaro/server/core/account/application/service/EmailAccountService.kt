package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.ExistEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.UpdateEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.service.exception.AccountNotFoundException
import kr.kro.dokbaro.server.core.account.application.service.exception.AlreadyRegisteredEmailException
import kr.kro.dokbaro.server.core.account.application.service.exception.PasswordNotMatchException
import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.AccountType
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class EmailAccountService(
	private val insertAccountPasswordPort: InsertAccountPasswordPort,
	private val registerMemberUseCase: RegisterMemberUseCase,
	private val passwordEncoder: PasswordEncoder,
	private val useAuthenticatedEmailUseCase: UseAuthenticatedEmailUseCase,
	private val temporaryPasswordGenerator: TemporaryPasswordGenerator,
	private val loadEmailAccountPort: LoadEmailAccountPort,
	private val updateEmailAccountPort: UpdateEmailAccountPort,
	private val sendTemporaryPasswordPort: SendTemporaryPasswordPort,
	private val existEmailAccountPort: ExistEmailAccountPort,
) : RegisterEmailAccountUseCase,
	IssueTemporaryPasswordUseCase,
	ChangePasswordUseCase {
	override fun registerEmailAccount(command: RegisterEmailAccountCommand): UUID {
		useAuthenticatedEmailUseCase.useEmail(email = command.email)

		if (existEmailAccountPort.existsByEmail(command.email)) {
			throw AlreadyRegisteredEmailException(command.email)
		}

		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					nickname = command.nickname,
					email = command.email,
					profileImage = command.profileImage,
					accountType = AccountType.EMAIL,
				),
			)

		insertAccountPasswordPort.insertEmailAccount(
			EmailAccount.of(
				email = command.email,
				rawPassword = command.password,
				memberId = member.id,
				encoder = passwordEncoder,
			),
		)

		return member.certificationId
	}

	override fun issueTemporaryPassword(email: String) {
		val emailAccount: EmailAccount = loadEmailAccountPort.findByEmail(email) ?: throw AccountNotFoundException()
		val newPassword: String = temporaryPasswordGenerator.generate()

		emailAccount.changePassword(
			newPassword = newPassword,
			encoder = passwordEncoder,
		)

		updateEmailAccountPort.updateEmailAccount(emailAccount)
		sendTemporaryPasswordPort.sendTemporaryPassword(email, newPassword)
	}

	override fun changePassword(command: ChangePasswordCommand) {
		val emailAccount: EmailAccount =
			loadEmailAccountPort.findByMemberId(command.memberId) ?: throw AccountNotFoundException()

		if (!emailAccount.match(
				rawPassword = command.oldPassword,
				encoder = passwordEncoder,
			)
		) {
			throw PasswordNotMatchException()
		}

		emailAccount.changePassword(
			newPassword = command.newPassword,
			encoder = passwordEncoder,
		)

		updateEmailAccountPort.updateEmailAccount(emailAccount)
	}
}