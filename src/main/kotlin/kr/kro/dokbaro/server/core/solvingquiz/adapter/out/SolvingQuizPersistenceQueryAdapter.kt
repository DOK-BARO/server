package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.CountSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadStudyGroupSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword

@PersistenceAdapter
class SolvingQuizPersistenceQueryAdapter(
	private val solvingQuizQueryRepository: SolvingQuizQueryRepository,
) : ReadMySolveSummaryPort,
	ReadMyStudyGroupSolveSummaryPort,
	CountSolvingQuizPort,
	LoadStudyGroupSolvingQuizPort,
	LoadSolvingQuizPort {
	override fun findAllMySolveSummary(
		memberId: Long,
		pageOption: PageOption<MySolvingQuizSortKeyword>,
	): Collection<MySolveSummary> = solvingQuizQueryRepository.findAllMySolveSummary(memberId, pageOption)

	override fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>,
	): Collection<StudyGroupSolveSummary> =
		solvingQuizQueryRepository.findAllMyStudyGroupSolveSummary(memberId, studyGroupId, pageOption)

	override fun countBy(condition: CountSolvingQuizCondition): Long = solvingQuizQueryRepository.countBy(condition)

	override fun findAllStudyGroupSolvingQuizSheets(
		studyGroupId: Long,
		quizId: Long,
	): Map<StudyGroupTotalGradeResult.Member, SolvingQuiz?> =
		solvingQuizQueryRepository.findAllStudyGroupSolvingQuizSheets(
			studyGroupId = studyGroupId,
			quizId = quizId,
		)

	override fun findById(solvingQuizId: Long): SolvingQuiz? = solvingQuizQueryRepository.findById(solvingQuizId)
}