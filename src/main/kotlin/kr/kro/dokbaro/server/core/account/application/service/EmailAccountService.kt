package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.UpdateAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.service.exception.AccountNotFoundException
import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
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
	private val updateAccountPasswordPort: UpdateAccountPasswordPort,
	private val sendTemporaryPasswordPort: SendTemporaryPasswordPort,
) : RegisterEmailAccountUseCase,
	IssueTemporaryPasswordUseCase,
	ChangePasswordUseCase {
	override fun registerEmailAccount(command: RegisterEmailAccountCommand): UUID {
		useAuthenticatedEmailUseCase.useEmail(email = command.email)

		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					nickname = command.nickname,
					email = command.email,
					profileImage = command.profileImage,
				),
			)

		insertAccountPasswordPort.insertAccountPassword(
			AccountPassword.of(
				rawPassword = command.password,
				memberId = member.id,
				encoder = passwordEncoder,
			),
		)

		return member.certificationId
	}

	override fun issueTemporaryPassword(email: String) {
		val accountPassword: AccountPassword = loadAccountPasswordPort.findByEmail(email) ?: throw AccountNotFoundException()
		val newPassword: String = temporaryPasswordGenerator.generate()

		accountPassword.changePassword(
			oldPassword = accountPassword.password,
			newPassword = newPassword,
			encoder = passwordEncoder,
		)

		updateAccountPasswordPort.updateAccountPassword(accountPassword)
		sendTemporaryPasswordPort.sendTemporaryPassword(email, newPassword)
	}

	override fun changePassword(command: ChangePasswordCommand) {
		val accountPassword: AccountPassword =
			loadAccountPasswordPort.findByMemberId(command.memberId) ?: throw AccountNotFoundException()

		accountPassword.changePassword(
			oldPassword = command.oldPassword,
			newPassword = command.newPassword,
			encoder = passwordEncoder,
		)

		updateAccountPasswordPort.updateAccountPassword(accountPassword)
	}
}