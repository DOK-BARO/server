package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary

@PersistenceAdapter
class SolvingQuizPersistenceQueryAdapter(
	private val solvingQuizQueryRepository: SolvingQuizQueryRepository,
) : ReadMySolveSummaryPort,
	ReadMyStudyGroupSolveSummaryPort {
	override fun findAllMySolveSummary(memberId: Long): Collection<MySolveSummary> =
		solvingQuizQueryRepository.findAllMySolveSummary(memberId)

	override fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary> =
		solvingQuizQueryRepository.findAllMyStudyGroupSolveSummary(memberId, studyGroupId)
}