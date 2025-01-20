package kr.kro.dokbaro.server.core.account.adapter.input.web

import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.ChangePasswordRequest
import kr.kro.dokbaro.server.core.account.adapter.input.web.dto.IssueTemporaryPasswordRequest
import kr.kro.dokbaro.server.core.account.application.port.input.ChangePasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.IssueTemporaryPasswordUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.RegisterEmailAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import kr.kro.dokbaro.server.security.jwt.JwtTokenGenerator
import kr.kro.dokbaro.server.security.jwt.cookie.JwtHttpCookieInjector
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
	private val issueTemporaryPasswordUseCase: IssueTemporaryPasswordUseCase,
	private val changePasswordUseCase: ChangePasswordUseCase,
) {
	/**
	 * email 계정 회원가입 API
	 */
	@PostMapping("/email")
	@ResponseStatus(HttpStatus.CREATED)
	fun signUpEmail(
		@RequestBody command: RegisterEmailAccountCommand,
		response: HttpServletResponse,
	) {
		val certificationId: UUID = registerEmailAccountUseCase.registerEmailAccount(command)

		jwtHttpCookieInjector.inject(response, jwtTokenGenerator.generate(certificationId))
	}

	/**
	 * 이메일 계정 임시 비밀번호 발급 API
	 */
	@PostMapping("/email/issue-temporary-password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun issueTemporaryPassword(
		@RequestBody body: IssueTemporaryPasswordRequest,
	) {
		issueTemporaryPasswordUseCase.issueTemporaryPassword(body.email)
	}

	/**
	 * 이메일 계정 비밀번호 변경 API
	 */
	@PutMapping("/email/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun changePassword(
		@Login user: DokbaroUser,
		@RequestBody body: ChangePasswordRequest,
	) {
		changePasswordUseCase.changePassword(
			ChangePasswordCommand(
				memberId = user.id,
				oldPassword = body.oldPassword,
				newPassword = body.newPassword,
			),
		)
	}
}