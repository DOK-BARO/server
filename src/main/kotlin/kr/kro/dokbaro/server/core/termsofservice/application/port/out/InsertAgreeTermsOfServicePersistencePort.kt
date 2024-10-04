package kr.kro.dokbaro.server.core.termsofservice.application.port.out

import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService

fun interface InsertAgreeTermsOfServicePersistencePort {
	fun insertAgree(agreeTermsOfService: AgreeTermsOfService)
}