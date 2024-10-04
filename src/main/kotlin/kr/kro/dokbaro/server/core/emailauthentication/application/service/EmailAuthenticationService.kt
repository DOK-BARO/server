package kr.kro.dokbaro.server.core.emailauthentication.application.service

import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.CreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.MatchCodeUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.RecreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.ExistEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.InsertEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.LoadEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SendEmailAuthenticationCodePort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.UpdateEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.springframework.stereotype.Service

@Service
class EmailAuthenticationService(
	private val existEmailAuthenticationPort: ExistEmailAuthenticationPort,
	private val insertEmailAuthenticationPort: InsertEmailAuthenticationPort,
	private val loadEmailAuthenticationPort: LoadEmailAuthenticationPort,
	private val updateEmailAuthenticationPort: UpdateEmailAuthenticationPort,
	private val emailCodeGenerator: EmailCodeGenerator,
	private val sendEmailAuthenticationCodePort: SendEmailAuthenticationCodePort,
) : CreateEmailAuthenticationUseCase,
	MatchCodeUseCase,
	RecreateEmailAuthenticationUseCase,
	UseAuthenticatedEmailUseCase {
	override fun create(email: String) {
		if (existEmailAuthenticationPort.existBy(
				SearchEmailAuthenticationCondition(
					address = email,
					authenticated = false,
					used = false,
				),
			)
		) {
			recreate(email)
			return
		}

		val code: String = emailCodeGenerator.generate()
		insertEmailAuthenticationPort.insert(EmailAuthentication(email, code))
		sendEmailAuthenticationCodePort.sendEmail(email, code)
	}

	override fun match(
		email: String,
		code: String,
	): MatchResponse {
		val emailAuthentication: EmailAuthentication =
			loadEmailAuthenticationPort.findBy(
				SearchEmailAuthenticationCondition(
					address = email,
					authenticated = false,
					used = false,
				),
			) ?: throw NotFoundEmailAuthenticationException(email)

		val matchResult: Boolean = emailAuthentication.matchCode(code)

		if (matchResult) {
			updateEmailAuthenticationPort.update(emailAuthentication)
		}

		return MatchResponse(matchResult)
	}

	override fun recreate(email: String) {
		val emailAuthentication: EmailAuthentication =
			loadEmailAuthenticationPort.findBy(
				SearchEmailAuthenticationCondition(
					address = email,
					authenticated = false,
					used = false,
				),
			) ?: throw NotFoundEmailAuthenticationException(email)

		val newCode: String = emailCodeGenerator.generate()
		emailAuthentication.changeCode(newCode)

		updateEmailAuthenticationPort.update(emailAuthentication)
		sendEmailAuthenticationCodePort.sendEmail(email, newCode)
	}

	override fun useEmail(email: String) {
		val emailAuthentication: EmailAuthentication =
			loadEmailAuthenticationPort.findBy(
				SearchEmailAuthenticationCondition(
					address = email,
					authenticated = true,
					used = false,
				),
			) ?: throw NotFoundEmailAuthenticationException(email)

		emailAuthentication.use()

		updateEmailAuthenticationPort.update(emailAuthentication)
	}
}