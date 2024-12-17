package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.CountSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword

@PersistenceAdapter
class SolvingQuizPersistenceQueryAdapter(
	private val solvingQuizQueryRepository: SolvingQuizQueryRepository,
) : ReadMySolveSummaryPort,
	ReadMyStudyGroupSolveSummaryPort,
	CountSolvingQuizPort {
	override fun findAllMySolveSummary(
		memberId: Long,
		pageOption: PageOption<MySolvingQuizSortKeyword>,
	): Collection<MySolveSummary> = solvingQuizQueryRepository.findAllMySolveSummary(memberId, pageOption)

	override fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary> =
		solvingQuizQueryRepository.findAllMyStudyGroupSolveSummary(memberId, studyGroupId)

	override fun countBy(condition: CountSolvingQuizCondition): Long = solvingQuizQueryRepository.countBy(condition)
}