package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.LoadOAuth2CertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception.NotFoundAccountException
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class OAuth2LoginService(
	private val accountLoader: OAuth2ProviderAccountLoader,
	private val loadOAuth2CertificatedAccountPort: LoadOAuth2CertificatedAccountPort,
	private val generateAuthTokenUseCase: GenerateAuthTokenUseCase,
) : OAuth2LoginUseCase {
	override fun login(command: LoadProviderAccountCommand): AuthToken {
		val providerAccount: OAuth2ProviderAccount = accountLoader.getAccount(command)
		val account: OAuth2CertificatedAccount =
			loadOAuth2CertificatedAccountPort.findBy(providerAccount.id, providerAccount.provider)
				?: throw NotFoundAccountException()

		return generateAuthTokenUseCase.generate(
			TokenClaims(
				UUIDUtils.uuidToString(account.certificationId),
				account.role,
			),
		)
	}
}