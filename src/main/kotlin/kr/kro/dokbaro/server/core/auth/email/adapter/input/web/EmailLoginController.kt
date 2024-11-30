package kr.kro.dokbaro.server.core.auth.email.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseEntityGenerator
import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailLoginUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailLoginCommand
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/email/login")
class EmailLoginController(
	private val emailLoginUseCase: EmailLoginUseCase,
	private val jwtResponseEntityGenerator: JwtResponseEntityGenerator,
) {
	@PostMapping
	fun login(
		@RequestBody body: EmailLoginCommand,
	): ResponseEntity<MessageResponse> {
		val (accessToken: String, refreshToken: String) = emailLoginUseCase.login(body)

		return jwtResponseEntityGenerator
			.getResponseBuilder(
				accessToken = accessToken,
				refreshToken = refreshToken,
			).body(MessageResponse(message = "Login Success / set cookie"))
	}
}