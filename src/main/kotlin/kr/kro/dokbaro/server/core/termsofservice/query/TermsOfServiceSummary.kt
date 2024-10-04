package kr.kro.dokbaro.server.core.termsofservice.query

data class TermsOfServiceSummary(
	val id: Long,
	val title: String,
	val subTitle: String?,
	val hasDetail: Boolean,
	val primary: Boolean,
)