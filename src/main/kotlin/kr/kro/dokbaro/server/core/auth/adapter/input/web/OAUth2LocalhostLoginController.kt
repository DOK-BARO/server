package kr.kro.dokbaro.server.core.auth.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@Profile("dev", "local", "test")
@RestController
@RequestMapping("/auth/oauth2/login/localhost")
class OAUth2LocalhostLoginController(
	private val loginUseCase: OAuth2LoginUseCase,
	@Value("\${jwt.access-header-name}") private val accessHeaderName: String,
	@Value("\${jwt.refresh-header-name}") private val refreshHeaderName: String,
) {
	@PostMapping("/{provider}")
	fun oauth2Login(
		@PathVariable provider: AuthProvider,
		@RequestBody body: ProviderAuthorizationTokenRequest,
	): ResponseEntity<MessageResponse> {
		val (accessToken: String, refreshToken: String) =
			loginUseCase.login(
				LoadProviderAccountCommand(
					provider,
					body.token,
					body.redirectUrl,
				),
			)

		return ResponseEntity
			.ok()
			.header(HttpHeaders.SET_COOKIE, toCookie(accessHeaderName, accessToken))
			.header(HttpHeaders.SET_COOKIE, toCookie(refreshHeaderName, refreshToken))
			.body(MessageResponse("Login Success / set cookie"))
	}

	private fun toCookie(
		name: String,
		value: String,
	): String =
		ResponseCookie
			.from(name, value)
			.sameSite("None")
			.domain("localhost.com")
			.maxAge(Duration.ofDays(500))
			.path("/")
			.secure(true)
			.httpOnly(true)
			.build()
			.toString()
}