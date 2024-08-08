package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.account.application.port.input.query.AccountFinder
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
	private val providerAccountLoader: ProviderAccountLoader,
	private val accountFinder: AccountFinder,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2LoginUseCase {
	override fun login(command: ProviderAuthorizationCommand): AuthToken {
		val providerAccount: ProviderAccount = providerAccountLoader.load(command)
		val account: AccountResponse = accountFinder.getById(providerAccount.id)

		return generateAuthTokenUseCase.generate(TokenClaims(account.id.toString(), account.role))
	}
}