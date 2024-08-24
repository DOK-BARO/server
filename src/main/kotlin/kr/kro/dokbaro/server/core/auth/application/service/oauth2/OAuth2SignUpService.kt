package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.account.application.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.OAuth2AccountLoader
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2SignUpService(
	private val accountLoader: OAuth2AccountLoader,
	private val registerMemberUseCase: RegisterMemberUseCase,
	private val registerAccountUseCase: RegisterAccountUseCase,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2SignUpUseCase {
	override fun signUp(command: LoadProviderAccountCommand): AuthToken {
		val providerAccount: ProviderAccount = accountLoader.get(command)

		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					providerAccount.name,
					providerAccount.name,
					providerAccount.email,
					providerAccount.profileImage,
				),
			)

		registerAccountUseCase.register(RegisterAccountCommand(providerAccount.id, providerAccount.provider, member.id))

		return generateAuthTokenUseCase.generate(
			TokenClaims(
				member.certificationId.toString(),
				member.roles.map { it.name },
			),
		)
	}
}