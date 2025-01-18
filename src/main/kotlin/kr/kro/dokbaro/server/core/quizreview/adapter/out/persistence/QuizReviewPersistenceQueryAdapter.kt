package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadMyQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.MyQuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword

@PersistenceAdapter
class QuizReviewPersistenceQueryAdapter(
	private val quizReviewQueryRepository: QuizReviewQueryRepository,
) : ReadQuizReviewTotalScorePort,
	CountQuizReviewPort,
	ReadQuizReviewSummaryPort,
	ReadMyQuizReviewPort {
	override fun findBy(quizId: Long): Collection<QuizReviewTotalScoreElement> =
		quizReviewQueryRepository.findTotalScoreBy(quizId)

	override fun countBy(condition: CountQuizReviewCondition): Long = quizReviewQueryRepository.countBy(condition)

	override fun findAllQuizReviewSummaryBy(
		condition: ReadQuizReviewSummaryCondition,
		pageOption: PageOption<QuizReviewSummarySortKeyword>,
	): Collection<QuizReviewSummary> =
		quizReviewQueryRepository.findAllQuizReviewSummaryBy(
			condition = condition,
			pageOption = pageOption,
		)

	override fun findMyReviewBy(
		quizId: Long,
		memberId: Long,
	): MyQuizReview? = quizReviewQueryRepository.findMyReviewBy(quizId, memberId)
}