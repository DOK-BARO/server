package kr.kro.dokbaro.server.core.member.adapter.input.web.dto

data class ModifyMemberRequest(
	val nickName: String?,
	val email: String?,
	val profileImage: String?,
)