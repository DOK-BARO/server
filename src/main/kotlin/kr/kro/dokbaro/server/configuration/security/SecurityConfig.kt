package kr.kro.dokbaro.server.configuration.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
	@Value("\${spring.security.allow-origins}") private val originPattern: List<String>,
	private val authorizationFilter: TokenBasedAuthorizationFilter,
) {
	@Bean
	fun configure(http: HttpSecurity): SecurityFilterChain =
		http
			.authorizeHttpRequests {
				it.anyRequest().permitAll()
			}.sessionManagement {
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			}.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
			.logout {
				it.logoutUrl("/logout")
				it.logoutSuccessHandler(logoutSuccessHandler())
			}.cors { it.configurationSource(corsConfig()) }
			.csrf { it.disable() }
			.formLogin { it.disable() }
			.httpBasic { it.disable() }
			.build()

	@Bean
	fun corsConfig(): CorsConfigurationSource {
		val corsConfiguration = CorsConfiguration()

		corsConfiguration.setAllowedOriginPatterns(originPattern)
		corsConfiguration.addAllowedHeader("*")
		corsConfiguration.addAllowedMethod("*")
		corsConfiguration.allowCredentials = true

		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", corsConfiguration)
		return source
	}

	@Bean
	fun logoutSuccessHandler() =
		LogoutSuccessHandler { _, res, _ ->
			res.status = HttpServletResponse.SC_OK
		}

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}