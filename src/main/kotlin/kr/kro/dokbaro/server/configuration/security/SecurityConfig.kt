package kr.kro.dokbaro.server.configuration.security

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.configuration.security.oauth2.OAuth2SuccessHandler
import kr.kro.dokbaro.server.configuration.security.token.TokenBasedAuthorizationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
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
	private val oAuth2SuccessHandler: OAuth2SuccessHandler,
	private val oAuth2UserService: OAuth2UserService<OAuth2UserRequest, OAuth2User>,
	private val authorizationFilter: TokenBasedAuthorizationFilter,
) {
	@Bean
	fun configure(http: HttpSecurity): SecurityFilterChain =
		http
			.authorizeHttpRequests {
				it.anyRequest().permitAll()
			}.oauth2Login {
				it.authorizationEndpoint { conf ->
					conf.baseUri("/auth/login")
				}
				it.successHandler(oAuth2SuccessHandler)
				it.userInfoEndpoint { conf ->
					conf.userService(oAuth2UserService)
				}
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