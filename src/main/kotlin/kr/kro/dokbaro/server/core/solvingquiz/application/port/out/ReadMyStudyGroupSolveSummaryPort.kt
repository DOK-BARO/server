package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary

fun interface ReadMyStudyGroupSolveSummaryPort {
	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary>
}