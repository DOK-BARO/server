package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq.QuizReviewMapper
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.generated.tables.JQuizReview
import org.jooq.generated.tables.records.QuizReviewRecord
import org.springframework.stereotype.Repository

@Repository
class QuizReviewQueryRepository(
	private val dslContext: DSLContext,
	private val quizReviewMapper: QuizReviewMapper,
) {
	companion object {
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
	}

	fun findTotalScoreBy(quizId: Long): Collection<QuizReviewTotalScoreElement> {
		val record: Result<QuizReviewRecord> =
			dslContext
				.select()
				.from(QUIZ_REVIEW)
				.where(QUIZ_REVIEW.QUIZ_ID.eq(quizId))
				.fetchInto(QUIZ_REVIEW)

		return quizReviewMapper.recordToQuizReviewTotalScoreElement(record)
	}
}