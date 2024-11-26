package kr.kro.dokbaro.server.core.security

import kr.kro.dokbaro.server.core.member.domain.Role
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer

object AuthorizationEndpointChain {
	fun authorizeHttpRequests(
		origin: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry,
	): AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry =
		origin
			.requestMatchers(*permitAllAnyRequests())
			.permitAll()
			.requestMatchers(HttpMethod.GET, *permitAllGetRequests())
			.permitAll()
			.requestMatchers(HttpMethod.POST, *permitAdminPostRequest())
			.hasRole(Role.ADMIN.name)
			.anyRequest()
			.authenticated()

	private fun permitAllAnyRequests() =
		arrayOf(
			"/auth/**",
			"/email-authentications/**",
			"/terms-of-services/**",
			"/token/**",
			"/health-check",
		)

	private fun permitAllGetRequests() =
		arrayOf(
			"/book-categories",
			"/books/**",
			"/book-quizzes",
			"/images",
			"/quiz-reviews/**",
		)

	private fun permitAdminPostRequest() =
		arrayOf(
			"/book-categories",
			"/books",
		)
}