package kr.kro.dokbaro.server.core.token.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseGenerator
import kr.kro.dokbaro.server.core.token.application.port.input.ReGenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(
	private val reGenerateAuthTokenUseCase: ReGenerateAuthTokenUseCase,
	private val jwtResponseGenerator: JwtResponseGenerator,
) {
	@PostMapping("/refresh")
	fun refreshToken(
		@CookieValue(value = "\${jwt.refresh-header-name}") refreshToken: String,
	): ResponseEntity<MessageResponse> {
		val reGenerateToken: AuthToken = reGenerateAuthTokenUseCase.reGenerate(refreshToken)

		return jwtResponseGenerator
			.getResponseBuilder(reGenerateToken.accessToken, reGenerateToken.refreshToken)
			.body(MessageResponse("refresh Success / set cookie"))
	}
}