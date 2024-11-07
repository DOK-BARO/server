package kr.kro.dokbaro.server.core.studygroup.query

data class StudyGroupDetail(
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
	val studyMembers: Collection<StudyGroupDetailMember>,
	val inviteCode: String,
	val id: Long,
)

data class StudyGroupDetailMember(
	val id: Long,
	val nickname: String,
	val profileImageUrl: String?,
	val role: String,
)