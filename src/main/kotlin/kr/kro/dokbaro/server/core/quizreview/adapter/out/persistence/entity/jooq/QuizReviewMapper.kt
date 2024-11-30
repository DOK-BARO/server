package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.TimeUtils
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JQuizReview
import org.jooq.generated.tables.records.QuizReviewRecord

@Mapper
class QuizReviewMapper {
	companion object {
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
		private val MEMBER = JMember.MEMBER
	}

	fun recordToQuizReviewTotalScoreElement(record: Result<QuizReviewRecord>): Collection<QuizReviewTotalScoreElement> =
		record.map {
			QuizReviewTotalScoreElement(
				quizId = it.quizId,
				starRating = it.starRating,
				difficultyLevel = it.difficultyLevel,
			)
		}

	fun recordToSummary(record: Result<out Record>): Collection<QuizReviewSummary> =
		record.map {
			QuizReviewSummary(
				id = it[QUIZ_REVIEW.ID],
				quizId = it[QUIZ_REVIEW.QUIZ_ID],
				starRating = it[QUIZ_REVIEW.STAR_RATING],
				difficultyLevel = it[QUIZ_REVIEW.DIFFICULTY_LEVEL],
				writerId = it[QUIZ_REVIEW.MEMBER_ID],
				writerNickname = it[MEMBER.NICKNAME],
				comment = it[QUIZ_REVIEW.COMMENT],
				createdAt = TimeUtils.timeToInstant(it[QUIZ_REVIEW.CREATED_AT]),
			)
		}

	fun toQuizReview(record: QuizReviewRecord?): QuizReview? =
		record?.map {
			QuizReview(
				starRating = it[QUIZ_REVIEW.STAR_RATING],
				difficultyLevel = it[QUIZ_REVIEW.DIFFICULTY_LEVEL],
				comment = it[QUIZ_REVIEW.COMMENT],
				memberId = it[QUIZ_REVIEW.MEMBER_ID],
				quizId = it[QUIZ_REVIEW.QUIZ_ID],
				id = it[QUIZ_REVIEW.ID],
			)
		}
}