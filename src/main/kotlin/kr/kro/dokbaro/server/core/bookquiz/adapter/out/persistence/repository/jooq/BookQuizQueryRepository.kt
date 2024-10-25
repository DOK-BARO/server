package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import org.jooq.DSLContext
import org.jooq.OrderField
import org.jooq.Record3
import org.jooq.Record9
import org.jooq.Result
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JQuizReview
import org.jooq.impl.DSL.avg
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.selectCount
import org.springframework.stereotype.Repository

@Repository
class BookQuizQueryRepository(
	private val dslContext: DSLContext,
	private val bookQuizMapper: BookQuizMapper,
) {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE = JBookQuizAnswerExplainImage.BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE
		private val MEMBER = JMember.MEMBER
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
	}

	fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? {
		val record: Result<Record9<Long, String, Int, Long, ByteArray, String, Long, ByteArray, Int>> =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.TIME_LIMIT_SECOND,
					BOOK_QUIZ_QUESTION.ID,
					BOOK_QUIZ_QUESTION.QUESTION_CONTENT,
					BOOK_QUIZ_QUESTION.QUESTION_TYPE,
					BOOK_QUIZ_SELECT_OPTION.ID,
					BOOK_QUIZ_SELECT_OPTION.CONTENT,
					BOOK_QUIZ_SELECT_OPTION.SEQ,
				).from(BOOK_QUIZ)
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.leftJoin(BOOK_QUIZ_SELECT_OPTION)
				.on(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.where(BOOK_QUIZ.ID.eq(quizId))
				.fetch()

		return bookQuizMapper.recordToBookQuizQuestions(record)
	}

	fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer? {
		val record: Result<Record3<String, ByteArray, String>> =
			dslContext
				.select(
					BOOK_QUIZ_ANSWER.CONTENT,
					BOOK_QUIZ_QUESTION.EXPLANATION,
					BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL,
				).from(BOOK_QUIZ_ANSWER)
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.ID.eq(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID))
				.leftJoin(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE)
				.on(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.where(BOOK_QUIZ_QUESTION.ID.eq(questionId))
				.fetch()

		return bookQuizMapper.toBookQuizAnswer(record)
	}

	fun countBookQuizBy(bookId: Long): Long =
		dslContext
			.selectCount()
			.from(BOOK_QUIZ)
			.where(BOOK_QUIZ.BOOK_ID.eq(bookId))
			.fetchOneInto(Long::class.java)!!

	fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption,
		sortOption: SortOption<BookQuizSummarySortOption>,
	): Collection<BookQuizSummary> {
		val record =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					MEMBER.ID,
					MEMBER.NICKNAME,
					MEMBER.PROFILE_IMAGE_URL,
					avg(QUIZ_REVIEW.STAR_RATING).`as`(BookQuizRecordFieldName.AVERAGE_STAR_RATING.name),
					avg(QUIZ_REVIEW.DIFFICULTY_LEVEL).`as`(BookQuizRecordFieldName.AVERAGE_DIFFICULTY_LEVEL.name),
					field(
						selectCount()
							.from(BOOK_QUIZ_QUESTION)
							.where(
								BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID
									.eq(BOOK_QUIZ.ID),
							),
					).`as`(BookQuizRecordFieldName.BOOK_QUIZ_QUESTION_COUNT.name),
				).from(BOOK_QUIZ)
				.join(MEMBER)
				.on(BOOK_QUIZ.CREATOR_ID.eq(MEMBER.ID))
				.leftJoin(QUIZ_REVIEW)
				.on(QUIZ_REVIEW.QUIZ_ID.eq(BOOK_QUIZ.ID))
				.where(BOOK_QUIZ.BOOK_ID.eq(bookId))
				.groupBy(BOOK_QUIZ)
				.orderBy(toOrderQuery(sortOption))
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return bookQuizMapper.toBookQuizSummary(record)
	}

	private fun toOrderQuery(sortOption: SortOption<BookQuizSummarySortOption>): OrderField<out Any>? {
		val query =
			when (sortOption.keyword) {
				BookQuizSummarySortOption.STAR_RATING -> field(BookQuizRecordFieldName.AVERAGE_STAR_RATING.name)
				BookQuizSummarySortOption.CREATED_AT -> BOOK_QUIZ.CREATED_AT
			}

		if (sortOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}
}