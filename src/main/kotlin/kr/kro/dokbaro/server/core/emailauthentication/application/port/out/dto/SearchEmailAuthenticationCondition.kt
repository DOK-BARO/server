package kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto

data class SearchEmailAuthenticationCondition(
	val address: String? = null,
	val code: String? = null,
	var authenticated: Boolean? = null,
	var used: Boolean? = null,
)