package kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web.dto

data class MatchEmailCodeRequest(
	val email: String,
	val code: String,
)