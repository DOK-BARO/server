package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.TimeUtils
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import org.jooq.Record8
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JQuizReview
import org.jooq.generated.tables.records.QuizReviewRecord
import java.time.LocalDateTime

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

	fun recordToSummary(
		record: Result<Record8<Long, Long, Int, Int, Long, String, ByteArray, LocalDateTime>>,
	): Collection<QuizReviewSummary> =
		record.map {
			QuizReviewSummary(
				it.get(QUIZ_REVIEW.ID),
				it.get(QUIZ_REVIEW.QUIZ_ID),
				it.get(QUIZ_REVIEW.STAR_RATING),
				it.get(QUIZ_REVIEW.DIFFICULTY_LEVEL),
				it.get(QUIZ_REVIEW.MEMBER_ID),
				it.get(MEMBER.NICKNAME),
				it.get(QUIZ_REVIEW.COMMENT)?.toString(Charsets.UTF_8),
				TimeUtils.timeToInstant(it.get(QUIZ_REVIEW.CREATED_AT)),
			)
		}

	fun toQuizReview(record: QuizReviewRecord?): QuizReview? =
		record?.map {
			QuizReview(
				starRating = it.get(QUIZ_REVIEW.STAR_RATING),
				difficultyLevel = it.get(QUIZ_REVIEW.DIFFICULTY_LEVEL),
				comment = it.get(QUIZ_REVIEW.COMMENT)?.toString(Charsets.UTF_8),
				memberId = it.get(QUIZ_REVIEW.MEMBER_ID),
				quizId = it.get(QUIZ_REVIEW.QUIZ_ID),
				id = it.get(QUIZ_REVIEW.ID),
			)
		}
}