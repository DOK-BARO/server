package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.account.application.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.OAuth2AccountLoader
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2SignUpService(
	private val accountLoader: OAuth2AccountLoader,
	private val registerAccountUseCase: RegisterAccountUseCase,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2SignUpUseCase {
	override fun signUp(command: LoadProviderAccountCommand): AuthToken {
		val providerAccount: ProviderAccount = accountLoader.get(command)

		val account: AccountResult =
			registerAccountUseCase.register(RegisterAccountCommand(providerAccount.id, providerAccount.provider))

		return generateAuthTokenUseCase.generate(TokenClaims(account.id.toString(), account.role))
	}
}