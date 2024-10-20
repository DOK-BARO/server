package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import org.jooq.DSLContext
import org.jooq.generated.tables.JQuizReview
import org.springframework.stereotype.Repository

@Repository
class QuizReviewRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
	}

	fun insert(quizReview: QuizReview): Long =
		dslContext
			.insertInto(
				QUIZ_REVIEW,
				QUIZ_REVIEW.STAR_RATING,
				QUIZ_REVIEW.DIFFICULTY_LEVEL,
				QUIZ_REVIEW.COMMENT,
				QUIZ_REVIEW.MEMBER_ID,
				QUIZ_REVIEW.QUIZ_ID,
			).values(
				quizReview.starRating,
				quizReview.difficultyLevel,
				quizReview.comment?.toByteArray(),
				quizReview.memberId,
				quizReview.quizId,
			).returningResult(QUIZ_REVIEW.ID)
			.fetchOneInto(Long::class.java)!!
}