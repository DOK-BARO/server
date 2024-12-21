package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

data class UpdateStudyGroupCommand(
	val id: Long,
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
)