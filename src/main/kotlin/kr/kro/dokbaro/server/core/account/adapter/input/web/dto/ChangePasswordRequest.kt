package kr.kro.dokbaro.server.core.account.adapter.input.web.dto

data class ChangePasswordRequest(
	val oldPassword: String,
	val newPassword: String,
)