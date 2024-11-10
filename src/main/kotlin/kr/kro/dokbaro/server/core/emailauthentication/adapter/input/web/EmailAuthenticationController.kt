package kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web

import kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web.dto.CreateEmailRequest
import kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web.dto.MatchEmailCodeRequest
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.CreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.MatchCodeUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.RecreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/email-authentications")
class EmailAuthenticationController(
	private val createEmailAuthenticationUseCase: CreateEmailAuthenticationUseCase,
	private val matchCodeUseCase: MatchCodeUseCase,
	private val recreateEmailAuthenticationUseCase: RecreateEmailAuthenticationUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@RequestBody createEmailRequest: CreateEmailRequest,
	) {
		createEmailAuthenticationUseCase.create(createEmailRequest.email)
	}

	@PostMapping("/match-code")
	fun matchCode(
		@RequestBody matchEmailCodeRequest: MatchEmailCodeRequest,
	): MatchResponse = matchCodeUseCase.match(matchEmailCodeRequest.email, matchEmailCodeRequest.code)

	@PostMapping("/recreate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun recreate(
		@RequestBody createEmailRequest: CreateEmailRequest,
	) {
		recreateEmailAuthenticationUseCase.recreate(createEmailRequest.email)
	}
}