package kr.kro.dokbaro.server.core.solvingquiz.query

data class StudyGroupTotalGradeResult(
	val quizId: Long,
	val studyGroupId: Long,
	val totalQuestionCount: Int,
	val solvedMember: Collection<SolvedMember>,
	val unSolvedMember: Collection<Member>,
) {
	data class SolvedMember(
		val member: Member,
		val solvingQuizId: Long,
		val correctCount: Int,
	)

	data class Member(
		val id: Long,
		val nickname: String,
		val profileImageUrl: String?,
	)
}