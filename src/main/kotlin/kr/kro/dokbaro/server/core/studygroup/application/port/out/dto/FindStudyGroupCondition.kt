package kr.kro.dokbaro.server.core.studygroup.application.port.out.dto

data class FindStudyGroupCondition(
	val id: Long? = null,
	val inviteCode: String? = null,
)