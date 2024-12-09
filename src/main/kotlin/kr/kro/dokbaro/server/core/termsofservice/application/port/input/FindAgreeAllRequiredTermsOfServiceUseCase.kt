package kr.kro.dokbaro.server.core.termsofservice.application.port.input

import kr.kro.dokbaro.server.core.termsofservice.query.AgreeAllRequired

fun interface FindAgreeAllRequiredTermsOfServiceUseCase {
	fun findBy(memberId: Long): AgreeAllRequired
}