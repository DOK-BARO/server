package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

import java.util.UUID

data class CreateStudyGroupCommand(
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
	val creatorAuthId: UUID,
)