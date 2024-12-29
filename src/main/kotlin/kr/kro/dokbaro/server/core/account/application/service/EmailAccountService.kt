package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.UpdateAccountEmailUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.UpdateEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.service.exception.AccountNotFoundException
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
	private val loadAccountPasswordPort: LoadAccountPasswordPort,
	private val updateEmailAccountPort: UpdateEmailAccountPort,
	private val sendTemporaryPasswordPort: SendTemporaryPasswordPort,
) : RegisterEmailAccountUseCase,
	IssueTemporaryPasswordUseCase,
	ChangePasswordUseCase,
	UpdateAccountEmailUseCase {
	override fun registerEmailAccount(command: RegisterEmailAccountCommand): UUID {
		useAuthenticatedEmailUseCase.useEmail(email = command.email)

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
		val emailAccount: EmailAccount = loadAccountPasswordPort.findByEmail(email) ?: throw AccountNotFoundException()
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
			loadAccountPasswordPort.findByMemberId(command.memberId) ?: throw AccountNotFoundException()

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

	override fun updateEmail(
		memberId: Long,
		email: String,
	) {
		loadAccountPasswordPort.findByMemberId(memberId)?.let { account ->
			account.changeEmail(email)
			updateEmailAccountPort.updateEmailAccount(account)
		}
	}
}