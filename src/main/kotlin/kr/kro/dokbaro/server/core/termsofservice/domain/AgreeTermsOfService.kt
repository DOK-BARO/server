package kr.kro.dokbaro.server.core.termsofservice.domain

data class AgreeTermsOfService(
	val memberId: Long,
	val item: Collection<TermsOfService>,
)