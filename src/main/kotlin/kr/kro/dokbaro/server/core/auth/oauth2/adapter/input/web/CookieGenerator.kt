package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CookieGenerator(
	@Value("\${spring.security.server-domain}") private val domain: String,
) {
	fun generate(
		name: String,
		value: String,
		maxAgeMinute: Long,
		path: String = "/",
	): String =
		ResponseCookie
			.from(name, value)
			.sameSite("None")
			.domain(domain)
			.maxAge(Duration.ofSeconds(maxAgeMinute))
			.path(path)
			.secure(true)
			.httpOnly(true)
			.build()
			.toString()
}