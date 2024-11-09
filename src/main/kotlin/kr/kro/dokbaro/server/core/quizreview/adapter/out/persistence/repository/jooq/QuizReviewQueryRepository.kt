package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq.QuizReviewMapper
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortOption
import org.jooq.DSLContext
import org.jooq.OrderField
import org.jooq.Record8
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JQuizReview
import org.jooq.generated.tables.records.QuizReviewRecord
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class QuizReviewQueryRepository(
	private val dslContext: DSLContext,
	private val quizReviewMapper: QuizReviewMapper,
) {
	companion object {
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
		private val MEMBER = JMember.MEMBER
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

	fun countBy(condition: CountQuizReviewCondition): Long =
		dslContext
			.selectCount()
			.from(QUIZ_REVIEW)
			.where(QUIZ_REVIEW.QUIZ_ID.eq(condition.quizId))
			.fetchOneInto(Long::class.java)!!

	fun findAllQuizReviewSummaryBy(
		condition: ReadQuizReviewSummaryCondition,
		pageOption: PageOption,
		sortOption: SortOption<QuizReviewSummarySortOption>,
	): Collection<QuizReviewSummary> {
		val record: Result<Record8<Long, Long, Int, Int, Long, String, ByteArray, LocalDateTime>> =
			dslContext
				.select(
					QUIZ_REVIEW.ID,
					QUIZ_REVIEW.QUIZ_ID,
					QUIZ_REVIEW.STAR_RATING,
					QUIZ_REVIEW.DIFFICULTY_LEVEL,
					QUIZ_REVIEW.MEMBER_ID,
					MEMBER.NICKNAME,
					QUIZ_REVIEW.COMMENT,
					QUIZ_REVIEW.CREATED_AT,
				).from(QUIZ_REVIEW)
				.join(MEMBER)
				.on(QUIZ_REVIEW.MEMBER_ID.eq(MEMBER.ID))
				.where(
					QUIZ_REVIEW.QUIZ_ID.eq(condition.quizId).and(
						QUIZ_REVIEW.DELETED.eq(Constants.NOT_DELETED),
					),
				).orderBy(toOrderByQuery(sortOption))
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return quizReviewMapper.recordToSummary(record)
	}

	private fun toOrderByQuery(sortOption: SortOption<QuizReviewSummarySortOption>): OrderField<out Any>? {
		val query =
			when (sortOption.keyword) {
				QuizReviewSummarySortOption.CREATED_AT -> QUIZ_REVIEW.CREATED_AT
				QuizReviewSummarySortOption.STAR_RATING -> QUIZ_REVIEW.STAR_RATING
			}

		if (sortOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun findById(id: Long): QuizReview? {
		val record: QuizReviewRecord? =
			dslContext
				.select(
					QUIZ_REVIEW.STAR_RATING,
					QUIZ_REVIEW.DIFFICULTY_LEVEL,
					QUIZ_REVIEW.COMMENT,
					QUIZ_REVIEW.MEMBER_ID,
					QUIZ_REVIEW.QUIZ_ID,
					QUIZ_REVIEW.ID,
				).from(QUIZ_REVIEW)
				.where(QUIZ_REVIEW.ID.eq(id).and(QUIZ_REVIEW.DELETED.eq(Constants.NOT_DELETED)))
				.fetchOneInto(QUIZ_REVIEW)

		return quizReviewMapper.toQuizReview(record)
	}
}