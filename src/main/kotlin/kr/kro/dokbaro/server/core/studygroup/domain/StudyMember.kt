package kr.kro.dokbaro.server.core.studygroup.domain

data class StudyMember(
	val memberId: Long,
	val role: StudyMemberRole,
)