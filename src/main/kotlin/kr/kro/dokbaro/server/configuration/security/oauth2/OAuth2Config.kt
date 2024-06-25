package kr.kro.dokbaro.server.configuration.security.oauth2

import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

@Configuration
class OAuth2Config {
	@Bean
	fun oAuth2UserService(
		registerAccountUseCase: RegisterAccountUseCase,
	): OAuth2UserService<OAuth2UserRequest, OAuth2User> =
		OAuth2UserServiceDecorator(DefaultOAuth2UserService(), registerAccountUseCase)
}