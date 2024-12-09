package kr.kro.dokbaro.server.security.configuration

import kr.kro.dokbaro.server.security.filter.JwtValidationFilter
import kr.kro.dokbaro.server.security.filter.OAuth2AuthenticationRedirectSetUpFilter
import kr.kro.dokbaro.server.security.handler.FormAuthenticationFailureHandler
import kr.kro.dokbaro.server.security.handler.FormAuthenticationSuccessHandler
import kr.kro.dokbaro.server.security.jwt.JwtTokenReGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class WebSecurityConfig(
	private val authenticationManager: AuthenticationManager,
	@Value("\${spring.security.allow-origins}") private val originPattern: List<String>,
	@Value("\${jwt.access-header-name}") private val accessTokenKey: String,
	@Value("\${jwt.refresh-header-name}") private val refreshTokenKey: String,
	private val formAuthenticationFailureHandler: FormAuthenticationFailureHandler,
	private val formAuthenticationSuccessHandler: FormAuthenticationSuccessHandler,
	private val oAuth2AuthenticationSuccessHandler: FormAuthenticationSuccessHandler,
	private val jwtTokenReGenerator: JwtTokenReGenerator,
) {
	@Bean
	fun configure(http: HttpSecurity): SecurityFilterChain =
		http
			.authorizeHttpRequests {
				AuthorizationEndpointChain.authorizeHttpRequests(it)
			}.addFilterBefore(
				OAuth2AuthenticationRedirectSetUpFilter(),
				OAuth2AuthorizationRequestRedirectFilter::class.java,
			).addFilterBefore(
				JwtValidationFilter(authenticationManager, jwtTokenReGenerator),
				UsernamePasswordAuthenticationFilter::class.java,
			).oauth2Login { l ->
				l.authorizationEndpoint { endpoint ->
					endpoint.baseUri("/auth/login/oauth2")
				}
				l.successHandler(oAuth2AuthenticationSuccessHandler)
			}.formLogin {
				it.loginProcessingUrl("/auth/login/email")
				it.usernameParameter("email")
				it.passwordParameter("password")
				it.successHandler(formAuthenticationSuccessHandler)
				it.failureHandler(formAuthenticationFailureHandler)
			}.sessionManagement {
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			}.cors { it.configurationSource(corsConfig()) }
			.csrf { it.disable() }
			.httpBasic { it.disable() }
			.build()

	fun corsConfig(): CorsConfigurationSource {
		val corsConfiguration = CorsConfiguration()
		corsConfiguration.allowedOrigins = originPattern
		corsConfiguration.addAllowedHeader("*")
		corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
		corsConfiguration.allowCredentials = true

		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", corsConfiguration)
		return source
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}