package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.DeleteQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.LoadQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.UpdateQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview

@PersistenceAdapter
class QuizReviewPersistenceAdapter(
	private val quizReviewRepository: QuizReviewRepository,
	private val quizReviewQueryRepository: QuizReviewQueryRepository,
) : InsertQuizReviewPort,
	LoadQuizReviewPort,
	UpdateQuizReviewPort,
	DeleteQuizReviewPort {
	override fun insert(quizReview: QuizReview): Long = quizReviewRepository.insert(quizReview)

	override fun findBy(id: Long): QuizReview? = quizReviewQueryRepository.findById(id)

	override fun update(quizReview: QuizReview) = quizReviewRepository.update(quizReview)

	override fun deleteBy(id: Long) = quizReviewRepository.deleteBy(id)
}