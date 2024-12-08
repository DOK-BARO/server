package kr.kro.dokbaro.server.core.termsofservice.application.port.input

fun interface AgreeTermsOfServiceUseCase {
	fun agree(
		loginUserId: Long,
		items: Collection<Long>,
	)
}