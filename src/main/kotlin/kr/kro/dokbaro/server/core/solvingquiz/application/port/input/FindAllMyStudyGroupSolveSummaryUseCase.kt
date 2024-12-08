package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary

fun interface FindAllMyStudyGroupSolveSummaryUseCase {
	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary>
}