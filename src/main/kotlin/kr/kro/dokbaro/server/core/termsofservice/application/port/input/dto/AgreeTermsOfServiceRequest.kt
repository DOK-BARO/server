package kr.kro.dokbaro.server.core.termsofservice.application.port.input.dto

data class AgreeTermsOfServiceRequest(
	val items: Collection<Long>,
)