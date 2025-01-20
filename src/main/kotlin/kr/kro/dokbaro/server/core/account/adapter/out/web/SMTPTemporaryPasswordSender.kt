package kr.kro.dokbaro.server.core.account.adapter.out.web

import jakarta.mail.internet.MimeMessage
import kr.kro.dokbaro.server.core.account.application.port.out.SendTemporaryPasswordPort
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

/**
 * SMTP 방식으로 임시 비밀번호를 전달합니다.
 * ThymeLeaf template 를 통해 email 형식을 구성합니다.
 */
@Component
class SMTPTemporaryPasswordSender(
	private val javaMailSender: JavaMailSender,
	private val templateEngine: TemplateEngine,
) : SendTemporaryPasswordPort {
	@Async
	override fun sendTemporaryPassword(
		email: String,
		password: String,
	) {
		val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()

		val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")
		mimeMessageHelper.setTo(email)
		mimeMessageHelper.setSubject("[DOKBARO] 임시비밀번호 전송해드립니다.")
		mimeMessageHelper.setText(toTemplate(email, password), true) // 메일 본문 내용, HTML 여부
		javaMailSender.send(mimeMessage)
	}

	fun toTemplate(
		email: String,
		password: String,
	): String {
		val context = Context()
		context.setVariable("email", email)
		context.setVariable("password", password)

		return templateEngine.process("temporaryPassword", context)
	}
}