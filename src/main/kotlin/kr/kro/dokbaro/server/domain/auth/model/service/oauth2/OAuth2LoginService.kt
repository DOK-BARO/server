package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import kr.kro.dokbaro.server.domain.account.port.input.query.AccountFinder
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.port.input.GenerateAuthTokenUseCase
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