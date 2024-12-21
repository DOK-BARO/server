package kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto

data class UpdateStudyGroupRequest(
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
)