package kr.kro.dokbaro.server.core.account.adapter.input.web

import kr.kro.dokbaro.server.core.account.application.port.input.command.DisableAccountUseCase
import kr.kro.dokbaro.server.global.dto.response.MessageResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class CancelAccountController(
	private val disableAccountUseCase: DisableAccountUseCase,
) {
	@PostMapping("/cancel")
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	fun cancel() = MessageResponse("NOT IMPLEMENTED")
}