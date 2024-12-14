package kr.kro.dokbaro.server.security.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
	private val securityChains: Collection<SecurityChain>,
) {
	@Bean
	fun configure(http: HttpSecurity): SecurityFilterChain =
		securityChains
			.fold(http) { target, chain ->
				chain.link(target)
			}.build()
}