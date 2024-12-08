package kr.kro.dokbaro.server.core.account.adapter.input.web

import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController("/accounts")
class AccountController(
	private val registerEmailAccountUseCase: RegisterEmailAccountUseCase,
) {
	@PostMapping("/email")
	@ResponseStatus(HttpStatus.CREATED)
	fun signUpEmail(command: RegisterEmailAccountCommand) {
		registerEmailAccountUseCase.registerEmailAccount(command)
	}
}