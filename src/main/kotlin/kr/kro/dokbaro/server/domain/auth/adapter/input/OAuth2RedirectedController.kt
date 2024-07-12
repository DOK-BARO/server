package kr.kro.dokbaro.server.domain.auth.adapter.input

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2RedirectUriUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/redirect")
class OAuth2RedirectedController(
	private val findOauth2RedirectUriUseCase: FindOAuth2RedirectUriUseCase,
) {
	@GetMapping("/{provider}")
	fun redirected(
		@PathVariable provider: String,
		response: HttpServletResponse,
	) {
		response.sendRedirect(
			findOauth2RedirectUriUseCase.getRedirectUri(provider),
		)
	}
}