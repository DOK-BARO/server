package kr.kro.dokbaro.server.core.member.adapter.input.web.dto

data class ModifyMemberRequest(
	val nickname: String?,
	val email: String?,
	val profileImage: String?,
)