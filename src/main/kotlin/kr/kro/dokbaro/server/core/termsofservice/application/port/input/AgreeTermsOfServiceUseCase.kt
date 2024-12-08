package kr.kro.dokbaro.server.core.termsofservice.application.port.input

fun interface AgreeTermsOfServiceUseCase {
	fun agree(
		memberId: Long,
		items: Collection<Long>,
	)
}