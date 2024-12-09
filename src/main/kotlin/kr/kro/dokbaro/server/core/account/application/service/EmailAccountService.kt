package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EmailAccountService(
	private val insertAccountPasswordPort: InsertAccountPasswordPort,
	private val registerMemberUseCase: RegisterMemberUseCase,
	private val passwordEncoder: PasswordEncoder,
	private val useAuthenticatedEmailUseCase: UseAuthenticatedEmailUseCase,
) : RegisterEmailAccountUseCase {
	override fun registerEmailAccount(command: RegisterEmailAccountCommand) {
		useAuthenticatedEmailUseCase.useEmail(email = command.email)

		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					nickname = command.nickname,
					email = command.email,
					profileImage = command.profileImage,
				),
			)

		val encryptedPassword = passwordEncoder.encode(command.password)

		insertAccountPasswordPort.insertAccountPassword(
			AccountPassword(
				memberId = member.id,
				password = encryptedPassword,
			),
		)
	}
}