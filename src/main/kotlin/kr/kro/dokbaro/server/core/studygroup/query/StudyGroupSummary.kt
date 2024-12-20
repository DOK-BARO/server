package kr.kro.dokbaro.server.core.studygroup.query

data class StudyGroupSummary(
	val id: Long,
	val name: String,
	val profileImageUrl: String?,
	val studyMemberCount: Int,
	val leader: Leader,
) {
	data class Leader(
		val id: Long,
		val nickname: String,
	)
}