package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview

@PersistenceAdapter
class QuizReviewPersistenceAdapter(
	private val quizReviewRepository: QuizReviewRepository,
) : InsertQuizReviewPort {
	override fun insert(quizReview: QuizReview): Long = quizReviewRepository.insert(quizReview)
}