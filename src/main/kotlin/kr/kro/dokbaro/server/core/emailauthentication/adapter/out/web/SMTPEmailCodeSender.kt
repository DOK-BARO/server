package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.web

import jakarta.mail.internet.MimeMessage
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SendEmailAuthenticationCodePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Component
class SMTPEmailCodeSender(
	private val javaMailSender: JavaMailSender,
	private val templateEngine: TemplateEngine,
	@Value("\${mail-authentication.title}")private val title: String,
) : SendEmailAuthenticationCodePort {
	override fun sendEmail(
		email: String,
		code: String,
	) {
		val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()

		val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")
		mimeMessageHelper.setTo(email)
		mimeMessageHelper.setSubject(title)
		mimeMessageHelper.setText(toTemplate(code), true) // 메일 본문 내용, HTML 여부

		javaMailSender.send(mimeMessage)
	}

	fun toTemplate(code: String): String {
		val context = Context()
		context.setVariable("code", code)

		return templateEngine.process("verifyEmail", context)
	}
}