package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.port.input.GenerateAuthTokenUseCase
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