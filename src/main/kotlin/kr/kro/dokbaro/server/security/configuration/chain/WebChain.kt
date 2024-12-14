package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@HttpSecurityChain
class WebChain(
	@Value("\${spring.security.allow-origins}") private val originPattern: List<String>,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin
			.sessionManagement {
				it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			}.cors { it.configurationSource(corsConfig()) }
			.csrf { it.disable() }
			.httpBasic { it.disable() }

	private fun corsConfig(): CorsConfigurationSource {
		val corsConfiguration = CorsConfiguration()
		corsConfiguration.allowedOrigins = originPattern
		corsConfiguration.addAllowedHeader("*")
		corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
		corsConfiguration.allowCredentials = true

		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", corsConfiguration)
		return source
	}
}