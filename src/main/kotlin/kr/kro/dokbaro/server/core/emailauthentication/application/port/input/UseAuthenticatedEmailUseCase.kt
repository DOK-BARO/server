package kr.kro.dokbaro.server.core.emailauthentication.application.port.input

fun interface UseAuthenticatedEmailUseCase {
	fun useEmail(email: String)
}