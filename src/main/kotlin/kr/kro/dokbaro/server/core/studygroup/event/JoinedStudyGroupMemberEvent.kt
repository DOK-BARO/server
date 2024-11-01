package kr.kro.dokbaro.server.core.studygroup.event

data class JoinedStudyGroupMemberEvent(
	val studyGroupId: Long,
	val studyGroupName: String,
	val memberId: Long,
	val memberName: String,
)