package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortOption

@PersistenceAdapter
class QuizReviewPersistenceQueryAdapter(
	private val quizReviewQueryRepository: QuizReviewQueryRepository,
) : ReadQuizReviewTotalScorePort,
	CountQuizReviewPort,
	ReadQuizReviewSummaryPort {
	override fun findBy(quizId: Long): Collection<QuizReviewTotalScoreElement> =
		quizReviewQueryRepository.findTotalScoreBy(quizId)

	override fun countBy(condition: CountQuizReviewCondition): Long = quizReviewQueryRepository.countBy(condition)

	override fun findAllQuizReviewSummaryBy(
		condition: ReadQuizReviewSummaryCondition,
		pageOption: PageOption,
		sortOption: SortOption<QuizReviewSummarySortOption>,
	): Collection<QuizReviewSummary> =
		quizReviewQueryRepository.findAllQuizReviewSummaryBy(condition, pageOption, sortOption)
}