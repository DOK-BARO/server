package kr.kro.dokbaro.server.common.http

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
		age: Duration,
		path: String = "/",
	): String =
		ResponseCookie
			.from(name, value)
			.sameSite("None")
			.domain(domain)
			.maxAge(age)
			.path(path)
			.secure(true)
			.httpOnly(true)
			.build()
			.toString()
}