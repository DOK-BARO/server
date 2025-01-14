package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@HttpSecurityChain
class AuthorizeHttpRequestChain : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin.authorizeHttpRequests {
			it
				.requestMatchers(*permitAllAnyRequests())
				.permitAll()
				.requestMatchers(HttpMethod.GET, *permitAllGetRequests())
				.permitAll()
				.requestMatchers(HttpMethod.POST, *permitAdminPostRequest())
				.hasRole(Role.ADMIN.name)
				.anyRequest()
				.authenticated()
		}

	private fun permitAllAnyRequests() =
		arrayOf(
			"/docs/**",
			"/email-authentications/**",
			"/terms-of-services/**",
			"/health-check",
			"/images/**",
			"/accounts/**",
			"/error/**",
		)

	private fun permitAllGetRequests() =
		arrayOf(
			"/book-categories",
			"/books/**",
			"/book-quizzes",
			"/book-quizzes/{id:\\d+}/explanation",
			"/quiz-reviews/**",
		)

	private fun permitAdminPostRequest() =
		arrayOf(
			"/book-categories",
			"/books",
		)
}