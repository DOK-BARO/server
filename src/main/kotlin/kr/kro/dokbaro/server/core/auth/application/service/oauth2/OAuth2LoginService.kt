package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.input.query.FindCertificationAccountUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.OAuth2AccountLoader
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
	private val accountLoader: OAuth2AccountLoader,
	private val findCertificationAccountUseCase: FindCertificationAccountUseCase,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2LoginUseCase {
	override fun login(command: LoadProviderAccountCommand): AuthToken {
		val providerAccount: ProviderAccount = accountLoader.get(command)
		val account: AccountResult = findCertificationAccountUseCase.getById(providerAccount.id)
	
		return generateAuthTokenUseCase.generate(TokenClaims(account.id.toString(), account.role))
	}
}