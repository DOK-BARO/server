package kr.kro.dokbaro.server.core.termsofservice.application.port.input

import java.util.UUID

fun interface AgreeTermsOfServiceUseCase {
	fun agree(
		authId: UUID,
		items: Collection<Long>,
	)
}