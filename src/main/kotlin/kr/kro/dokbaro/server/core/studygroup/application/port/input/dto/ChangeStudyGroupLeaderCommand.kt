package kr.kro.dokbaro.server.core.studygroup.application.port.input.dto

data class ChangeStudyGroupLeaderCommand(
	val studyGroupId: Long,
	val newLeaderId: Long,
)