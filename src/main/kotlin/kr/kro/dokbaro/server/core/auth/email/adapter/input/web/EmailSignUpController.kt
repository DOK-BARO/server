package kr.kro.dokbaro.server.core.auth.email.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseGenerator
import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailSignUpUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/email/signup")
class EmailSignUpController(
	private val emailSignUpUseCase: EmailSignUpUseCase,
	private val jwtResponseGenerator: JwtResponseGenerator,
) {
	@PostMapping
	fun signUp(
		@RequestBody body: EmailSignUpCommand,
	): ResponseEntity<MessageResponse> {
		val (accessToken: String, refreshToken: String) = emailSignUpUseCase.signUp(body)

		return jwtResponseGenerator
			.getResponseBuilder(accessToken, refreshToken)
			.body(MessageResponse("SignUp Success / set cookie"))
	}
}