package kr.kro.dokbaro.server.core.studygroup.query

data class StudyGroupDetail(
	val id: Long,
	val name: String,
	val introduction: String?,
	val profileImageUrl: String?,
	val studyMembers: Collection<StudyMember>,
	val inviteCode: String,
) {
	data class StudyMember(
		val id: Long,
		val nickname: String,
		val profileImageUrl: String?,
		val role: String,
	)
}