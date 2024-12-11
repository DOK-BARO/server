package kr.kro.dokbaro.server.core.account.adapter.input.web

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.security.jwt.JwtHttpCookieInjector
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/accounts")
class AccountController(
	private val registerEmailAccountUseCase: RegisterEmailAccountUseCase,
	private val jwtTokenGenerator: JwtTokenGenerator,
	private val jwtHttpCookieInjector: JwtHttpCookieInjector,
) {
	@PostMapping("/email")
	@ResponseStatus(HttpStatus.CREATED)
	fun signUpEmail(
		@RequestBody command: RegisterEmailAccountCommand,
		response: HttpServletResponse,
	) {
		val certificationId: UUID = registerEmailAccountUseCase.registerEmailAccount(command)

		jwtHttpCookieInjector.inject(response, jwtTokenGenerator.generate(certificationId))
	}
}