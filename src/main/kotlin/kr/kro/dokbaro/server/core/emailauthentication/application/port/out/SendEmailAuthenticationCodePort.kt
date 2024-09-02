package kr.kro.dokbaro.server.core.emailauthentication.application.port.out

fun interface SendEmailAuthenticationCodePort {
	fun sendEmail(
		email: String,
		code: String,
	)
}