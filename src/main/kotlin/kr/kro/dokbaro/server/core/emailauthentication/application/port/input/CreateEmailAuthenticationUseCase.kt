package kr.kro.dokbaro.server.core.emailauthentication.application.port.input

fun interface CreateEmailAuthenticationUseCase {
	fun create(email: String)
}