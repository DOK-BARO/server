package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.OAUth2SignUpUseCase
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.AuthTokenGenerator
import org.springframework.stereotype.Service

@Service
class OAuth2SignUpService(
	private val providerAccountLoader: ProviderAccountLoader,
	private val tokenGenerator: AuthTokenGenerator,
) : OAUth2SignUpUseCase {
	override fun signUp(command: ProviderAuthorizationCommand) {
		val providerAccount: ProviderAccount = providerAccountLoader.load(command)

		// tokenGenerator.generate(TokenClaims)
	}
}