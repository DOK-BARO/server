package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseEntityGenerator
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.OAuth2LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/login")
class OAuth2LoginController(
	private val loginUseCase: OAuth2LoginUseCase,
	private val jwtResponseEntityGenerator: JwtResponseEntityGenerator,
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

		return jwtResponseEntityGenerator
			.getResponseBuilder(accessToken, refreshToken)
			.body(MessageResponse("Login Success / set cookie"))
	}
}