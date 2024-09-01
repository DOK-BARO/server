package kr.kro.dokbaro.server.core.emailauthentication.application.service

import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.CreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.MatchCodeUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.RecreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.FindEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SaveEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SendEmailAuthenticationCodePort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.UpdateEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.springframework.stereotype.Service

@Service
class EmailAuthenticationService(
	private val saveEmailAuthenticationPort: SaveEmailAuthenticationPort,
	private val findEmailAuthenticationPort: FindEmailAuthenticationPort,
	private val updateEmailAuthenticationPort: UpdateEmailAuthenticationPort,
	private val emailCodeGenerator: EmailCodeGenerator,
	private val sendEmailAuthenticationCodePort: SendEmailAuthenticationCodePort,
) : CreateEmailAuthenticationUseCase,
	MatchCodeUseCase,
	RecreateEmailAuthenticationUseCase,
	UseAuthenticatedEmailUseCase {
	override fun create(email: String) {
		val code: String = emailCodeGenerator.generate()

		saveEmailAuthenticationPort.save(EmailAuthentication(email, code))
		sendEmailAuthenticationCodePort.sendEmail(email, code)
	}

	override fun match(
		email: String,
		code: String,
	): MatchResponse {
		val emailAuthentication: EmailAuthentication =
			findEmailAuthenticationPort.findBy(
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
			findEmailAuthenticationPort.findBy(
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
			findEmailAuthenticationPort.findBy(
				SearchEmailAuthenticationCondition(
					address = email,
					authenticated = false,
					used = false,
				),
			) ?: throw NotFoundEmailAuthenticationException(email)

		emailAuthentication.use()

		updateEmailAuthenticationPort.update(emailAuthentication)
	}
}