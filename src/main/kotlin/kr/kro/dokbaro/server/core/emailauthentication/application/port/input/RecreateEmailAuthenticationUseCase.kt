package kr.kro.dokbaro.server.core.emailauthentication.application.port.input

fun interface RecreateEmailAuthenticationUseCase {
	fun recreate(email: String)
}