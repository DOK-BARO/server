package kr.kro.dokbaro.server.security.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager

@Configuration
class AuthenticationProviderConfig(
	private val customProviders: Collection<AuthenticationProvider>,
) {
	@Bean
	fun AuthenticationManager(): AuthenticationManager = ProviderManager(customProviders.toMutableList())
}