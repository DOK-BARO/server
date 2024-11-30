package kr.kro.dokbaro.server.core.auth.email.application.service

import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailSignUpUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import kr.kro.dokbaro.server.core.auth.email.application.port.out.InsertEmailAccountPort
import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EmailSignUpService(
	private val registerMemberUseCase: RegisterMemberUseCase,
	private val useAuthenticatedEmailUseCase: UseAuthenticatedEmailUseCase,
	private val passwordEncoder: PasswordEncoder,
	private val insertEmailAccountPort: InsertEmailAccountPort,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : EmailSignUpUseCase {
	override fun signUp(command: EmailSignUpCommand): AuthToken {
		useAuthenticatedEmailUseCase.useEmail(email = command.email)

		val savedMember: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					nickName = command.nickname,
					email = command.email,
					profileImage = command.profileImage,
				),
			)

		insertEmailAccountPort.insert(
			AccountPassword(
				password = passwordEncoder.encode(command.password),
				memberId = savedMember.id,
			),
		)

		return generateAuthTokenUseCase.generate(
			TokenClaims(
				savedMember.certificationId.toString(),
				savedMember.roles.map { it.name },
			),
		)
	}
}