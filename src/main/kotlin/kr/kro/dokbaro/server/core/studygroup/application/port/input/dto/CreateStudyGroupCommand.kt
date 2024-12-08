package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

data class CreateStudyGroupCommand(
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
	val loginUserId: Long,
)