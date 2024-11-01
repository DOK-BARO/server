package kr.kro.dokbaro.server.core.studygroup.query

import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole

data class StudyGroupMemberResult(
	val groupId: Long,
	val groupMemberId: Long,
	val memberId: Long,
	val nickname: String,
	val role: StudyMemberRole,
)