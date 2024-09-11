package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.ExistOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.InsertOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception.AlreadyExistAccountException
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2SignUpService(
	private val accountLoader: OAuth2ProviderAccountLoader,
	private val registerMemberUseCase: RegisterMemberUseCase,
	private val existOAuth2AccountPort: ExistOAuth2AccountPort,
	private val insertOAuth2AccountPort: InsertOAuth2AccountPort,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2SignUpUseCase {
	override fun signUp(command: LoadProviderAccountCommand): AuthToken {
		val providerAccount: OAuth2ProviderAccount = accountLoader.getAccount(command)

		if (existOAuth2AccountPort.existBy(providerAccount.id, providerAccount.provider)) {
			throw AlreadyExistAccountException(providerAccount.id, providerAccount.provider)
		}

		val member: Member =
			registerMemberUseCase.register(
				RegisterMemberCommand(
					providerAccount.name + providerAccount.id,
					providerAccount.email,
					providerAccount.profileImage,
				),
			)

		insertOAuth2AccountPort.insert(
			OAuth2Account(
				providerAccount.id,
				providerAccount.provider,
				member.id,
			),
		)

		return generateAuthTokenUseCase.generate(
			TokenClaims(
				member.certificationId.toString(),
				member.roles.map { it.name },
			),
		)
	}
}