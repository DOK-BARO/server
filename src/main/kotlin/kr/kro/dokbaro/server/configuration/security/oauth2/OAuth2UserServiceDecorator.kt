package kr.kro.dokbaro.server.configuration.security.oauth2

import kr.kro.dokbaro.server.configuration.security.oauth2.provider.OAuthProvider
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.OAuthProviderResponse
import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.Locale

class OAuth2UserServiceDecorator(
	private val service: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
	private val registerAccountUseCase: RegisterAccountUseCase,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
		val provider = OAuthProvider.valueOf(userRequest.clientRegistration.clientName.uppercase(Locale.getDefault()))
		val user: OAuth2User = service.loadUser(userRequest)
		val providerResponse: OAuthProviderResponse = provider.map(user.attributes)
		registerAccountUseCase.registerIfNew(RegisterAccountCommand(providerResponse.id, provider.name))

		return user
	}
}