package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement

@PersistenceAdapter
class QuizReviewPersistenceQueryAdapter(
	private val quizReviewQueryRepository: QuizReviewQueryRepository,
) : ReadQuizReviewTotalScorePort {
	override fun findBy(quizId: Long): Collection<QuizReviewTotalScoreElement> =
		quizReviewQueryRepository.findTotalScoreBy(quizId)
}