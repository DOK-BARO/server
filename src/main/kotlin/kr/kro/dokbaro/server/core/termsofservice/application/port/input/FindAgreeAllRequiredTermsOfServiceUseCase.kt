package kr.kro.dokbaro.server.core.termsofservice.application.port.input

import kr.kro.dokbaro.server.core.termsofservice.query.AgreeAllRequired
import java.util.UUID

fun interface FindAgreeAllRequiredTermsOfServiceUseCase {
	fun findBy(authId: UUID): AgreeAllRequired
}