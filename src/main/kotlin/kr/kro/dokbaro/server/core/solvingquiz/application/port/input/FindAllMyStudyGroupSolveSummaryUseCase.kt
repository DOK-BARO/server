package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import java.util.UUID

fun interface FindAllMyStudyGroupSolveSummaryUseCase {
	fun findAllMyStudyGroupSolveSummary(
		authId: UUID,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary>
}