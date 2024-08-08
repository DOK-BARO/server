package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.account.application.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2SignUpService(
	private val providerAccountLoader: ProviderAccountLoader,
	private val registerAccountUseCase: RegisterAccountUseCase,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2SignUpUseCase {
	override fun signUp(command: ProviderAuthorizationCommand): AuthToken {
		val providerAccount: ProviderAccount = providerAccountLoader.load(command)

		val account: AccountResponse =
			registerAccountUseCase.register(RegisterAccountCommand(providerAccount.id, providerAccount.provider))

		return generateAuthTokenUseCase.generate(TokenClaims(account.id.toString(), account.role))
	}
}