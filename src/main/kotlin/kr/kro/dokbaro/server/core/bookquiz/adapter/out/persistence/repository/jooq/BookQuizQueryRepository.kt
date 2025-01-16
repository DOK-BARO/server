package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.BookQuizDetailQuestions
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.OrderField
import org.jooq.Record
import org.jooq.Record1
import org.jooq.Result
import org.jooq.Table
import org.jooq.generated.tables.JBook.BOOK
import org.jooq.generated.tables.JBookQuiz.BOOK_QUIZ
import org.jooq.generated.tables.JBookQuizAnswer.BOOK_QUIZ_ANSWER
import org.jooq.generated.tables.JBookQuizAnswerExplainImage.BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE
import org.jooq.generated.tables.JBookQuizContributor.BOOK_QUIZ_CONTRIBUTOR
import org.jooq.generated.tables.JBookQuizQuestion.BOOK_QUIZ_QUESTION
import org.jooq.generated.tables.JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
import org.jooq.generated.tables.JMember.MEMBER
import org.jooq.generated.tables.JQuizReview.QUIZ_REVIEW
import org.jooq.generated.tables.JSolvingQuiz.SOLVING_QUIZ
import org.jooq.generated.tables.JStudyGroup.STUDY_GROUP
import org.jooq.generated.tables.JStudyGroupQuiz.STUDY_GROUP_QUIZ
import org.jooq.generated.tables.records.BookQuizRecord
import org.jooq.impl.DSL
import org.jooq.impl.DSL.avg
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.selectCount
import org.springframework.stereotype.Repository

@Repository
class BookQuizQueryRepository(
	private val dslContext: DSLContext,
	private val bookQuizMapper: BookQuizMapper,
) {
	fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? {
		val record: Result<out Record> =
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
					STUDY_GROUP_QUIZ.STUDY_GROUP_ID,
				).from(BOOK_QUIZ)
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.leftJoin(BOOK_QUIZ_SELECT_OPTION)
				.on(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.leftJoin(STUDY_GROUP_QUIZ)
				.on(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.where(BOOK_QUIZ.ID.eq(quizId).and(BOOK_QUIZ.DELETED.isFalse))
				.fetch()

		return bookQuizMapper.toBookQuizQuestions(record)
	}

	fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer? {
		val record: Result<out Record> =
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

	fun countBookQuizBy(condition: CountBookQuizCondition): Long =
		dslContext
			.selectCount()
			.from(BOOK_QUIZ)
			.where(buildCountCondition(condition).and(BOOK_QUIZ.DELETED.isFalse))
			.fetchOneInto(Long::class.java)!!

	private fun buildCountCondition(condition: CountBookQuizCondition): Condition =
		DSL.and(
			condition.bookId?.let { BOOK_QUIZ.BOOK_ID.eq(it) },
			condition.creatorId?.let { BOOK_QUIZ.CREATOR_ID.eq(it) },
			if (condition.studyGroupId != null) {
				BOOK_QUIZ.ID
					.`in`(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ)
							.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(condition.studyGroupId)),
					)
			} else {
				BOOK_QUIZ.ID
					.notIn(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ),
					)
			},
			condition.solved?.let {
				if (it.solved) {
					BOOK_QUIZ.ID.`in`(
						select(SOLVING_QUIZ.QUIZ_ID)
							.from(SOLVING_QUIZ)
							.where(SOLVING_QUIZ.MEMBER_ID.eq(it.memberId)),
					)
				} else {
					BOOK_QUIZ.ID.notIn(
						select(SOLVING_QUIZ.QUIZ_ID)
							.from(SOLVING_QUIZ)
							.where(SOLVING_QUIZ.MEMBER_ID.eq(it.memberId)),
					)
				}
			},
		)

	fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption<BookQuizSummarySortKeyword>,
	): Collection<BookQuizSummary> {
		val record =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					MEMBER.ID,
					MEMBER.NICKNAME,
					MEMBER.PROFILE_IMAGE_URL,
					avg(QUIZ_REVIEW.STAR_RATING).`as`(BookQuizRecordFieldName.AVERAGE_STAR_RATING),
					avg(QUIZ_REVIEW.DIFFICULTY_LEVEL).`as`(BookQuizRecordFieldName.AVERAGE_DIFFICULTY_LEVEL),
					field(
						selectCount().from(QUIZ_REVIEW).where(QUIZ_REVIEW.QUIZ_ID.eq(BOOK_QUIZ.ID)),
					).`as`(BookQuizRecordFieldName.BOOK_QUIZ_REVIEW_COUNT),
					field(
						selectCount()
							.from(BOOK_QUIZ_QUESTION)
							.where(
								BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID
									.eq(BOOK_QUIZ.ID),
							),
					).`as`(BookQuizRecordFieldName.BOOK_QUIZ_QUESTION_COUNT),
				).from(BOOK_QUIZ)
				.join(MEMBER)
				.on(BOOK_QUIZ.CREATOR_ID.eq(MEMBER.ID))
				.leftJoin(QUIZ_REVIEW)
				.on(QUIZ_REVIEW.QUIZ_ID.eq(BOOK_QUIZ.ID))
				.where(
					BOOK_QUIZ.BOOK_ID.eq(bookId).and(BOOK_QUIZ.DELETED.isFalse).and(
						BOOK_QUIZ.ID
							.notIn(
								select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
									.from(STUDY_GROUP_QUIZ),
							),
					),
				).groupBy(BOOK_QUIZ)
				.orderBy(toBookQuizSummaryOrderQuery(pageOption), BOOK_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return bookQuizMapper.toBookQuizSummary(record)
	}

	private fun toBookQuizSummaryOrderQuery(pageOption: PageOption<BookQuizSummarySortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				BookQuizSummarySortKeyword.STAR_RATING -> field(BookQuizRecordFieldName.AVERAGE_STAR_RATING)
				BookQuizSummarySortKeyword.CREATED_AT -> BOOK_QUIZ.CREATED_AT
				BookQuizSummarySortKeyword.UPDATED_AT -> BOOK_QUIZ.UPDATED_AT
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>,
	): Collection<UnsolvedGroupBookQuizSummary> {
		val creator = MEMBER.`as`("CREATOR")
		val contributor = MEMBER.`as`("CONTRIBUTOR")

		val pagedBookQuiz: Table<Record1<Long>> =
			dslContext
				.select(BOOK_QUIZ.ID)
				.from(BOOK_QUIZ)
				.where(
					BOOK_QUIZ.ID
						.`in`(
							select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
								.from(STUDY_GROUP_QUIZ)
								.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(studyGroupId)),
						).and(
							BOOK_QUIZ.ID.notIn(
								select(SOLVING_QUIZ.QUIZ_ID)
									.from(SOLVING_QUIZ)
									.where(SOLVING_QUIZ.MEMBER_ID.eq(memberId)),
							),
						).and(BOOK_QUIZ.DELETED.isFalse),
				).orderBy(toOrderQuery(pageOption), BOOK_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.asTable("paged_book_quiz")

		val record: Map<BookQuizRecord, Result<out Record>> =
			dslContext
				.select(
					BOOK.ID,
					BOOK.TITLE,
					BOOK.IMAGE_URL,
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.DESCRIPTION,
					BOOK_QUIZ.CREATED_AT,
					creator.ID.`as`(BookQuizRecordFieldName.CREATOR_ID),
					creator.NICKNAME.`as`(BookQuizRecordFieldName.CREATOR_NAME),
					creator.PROFILE_IMAGE_URL.`as`(BookQuizRecordFieldName.CREATOR_IMAGE_URL),
					contributor.ID.`as`(BookQuizRecordFieldName.CONTRIBUTOR_ID),
					contributor.NICKNAME.`as`(BookQuizRecordFieldName.CONTRIBUTOR_NAME),
					contributor.PROFILE_IMAGE_URL.`as`(BookQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL),
				).from(pagedBookQuiz)
				.join(BOOK_QUIZ)
				.on(BOOK_QUIZ.ID.eq(pagedBookQuiz.field(BOOK_QUIZ.ID)))
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.join(creator)
				.on(creator.ID.eq(BOOK_QUIZ.CREATOR_ID))
				.leftJoin(BOOK_QUIZ_CONTRIBUTOR)
				.on(BOOK_QUIZ_CONTRIBUTOR.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.leftJoin(contributor)
				.on(contributor.ID.eq(BOOK_QUIZ_CONTRIBUTOR.MEMBER_ID))
				.fetchGroups(BOOK_QUIZ)

		return bookQuizMapper.toUnsolvedGroupBookQuizSummary(record)
	}

	private fun toOrderQuery(pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				UnsolvedGroupBookQuizSortKeyword.CREATED_AT -> BOOK_QUIZ.CREATED_AT
				UnsolvedGroupBookQuizSortKeyword.UPDATED_AT -> BOOK_QUIZ.UPDATED_AT
				UnsolvedGroupBookQuizSortKeyword.TITLE -> BOOK_QUIZ.TITLE
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun findAllMyBookQuizzes(
		memberId: Long,
		pageOption: PageOption<MyBookQuizSummarySortKeyword>,
	): Collection<MyBookQuizSummary> {
		val record: Result<out Record> =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK.IMAGE_URL,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.UPDATED_AT,
					STUDY_GROUP.ID,
					STUDY_GROUP.NAME,
					STUDY_GROUP.PROFILE_IMAGE_URL,
				).from(BOOK_QUIZ)
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.leftJoin(STUDY_GROUP_QUIZ)
				.on(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.leftJoin(STUDY_GROUP)
				.on(STUDY_GROUP.ID.eq(STUDY_GROUP_QUIZ.STUDY_GROUP_ID))
				.where(BOOK_QUIZ.CREATOR_ID.eq(memberId).and(BOOK_QUIZ.DELETED.isFalse))
				.orderBy(toMyBookQuizSummeryOrderQuery(pageOption), BOOK_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return bookQuizMapper.toMyBookQuiz(record)
	}

	private fun toMyBookQuizSummeryOrderQuery(pageOption: PageOption<MyBookQuizSummarySortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				MyBookQuizSummarySortKeyword.TITLE -> BOOK_QUIZ.TITLE
				MyBookQuizSummarySortKeyword.CREATED_AT -> BOOK_QUIZ.CREATED_AT
				MyBookQuizSummarySortKeyword.UPDATED_AT -> BOOK_QUIZ.UPDATED_AT
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun findBookQuizExplanationBy(id: Long): BookQuizExplanation? {
		val record: Result<out Record> =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.DESCRIPTION,
					BOOK_QUIZ.CREATED_AT,
					MEMBER.ID,
					MEMBER.NICKNAME,
					MEMBER.PROFILE_IMAGE_URL,
					BOOK.ID,
					BOOK.TITLE,
					BOOK.IMAGE_URL,
				).from(BOOK_QUIZ)
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.join(MEMBER)
				.on(MEMBER.ID.eq(BOOK_QUIZ.CREATOR_ID))
				.where(BOOK_QUIZ.ID.eq(id))
				.fetch()

		return bookQuizMapper.toBookQuizExplanation(record)
	}

	fun findBookQuizDetailBy(id: Long): BookQuizDetailQuestions? {
		val record: Map<BookQuizRecord, Result<out Record>> =
			dslContext
				.select(
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.DESCRIPTION,
					BOOK_QUIZ.BOOK_ID,
					STUDY_GROUP_QUIZ.STUDY_GROUP_ID,
					BOOK_QUIZ.TIME_LIMIT_SECOND,
					BOOK_QUIZ.VIEW_SCOPE,
					BOOK_QUIZ.EDIT_SCOPE,
					BOOK_QUIZ_QUESTION.ID,
					BOOK_QUIZ_QUESTION.QUESTION_CONTENT,
					BOOK_QUIZ_QUESTION.EXPLANATION,
					BOOK_QUIZ_QUESTION.QUESTION_TYPE,
				).from(BOOK_QUIZ)
				.leftJoin(STUDY_GROUP_QUIZ)
				.on(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.where(BOOK_QUIZ.ID.eq(id))
				.fetchGroups(BOOK_QUIZ)

		return bookQuizMapper.toBookQuizDetailQuestions(record)
	}

	fun findSelectOptionBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		dslContext
			.select(
				BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID,
				BOOK_QUIZ_SELECT_OPTION.CONTENT,
			).from(BOOK_QUIZ_SELECT_OPTION)
			.where(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.`in`(ids))
			.orderBy(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID, BOOK_QUIZ_SELECT_OPTION.SEQ)
			.fetchGroups(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID)
			.mapValues { (_, v) -> v.getValues(BOOK_QUIZ_SELECT_OPTION.CONTENT) }

	fun findAnswerExplanationImageBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		dslContext
			.select(
				BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID,
				BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL,
			).from(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE)
			.where(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID.`in`(ids))
			.fetchGroups(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID)
			.mapValues { (_, v) -> v.getValues(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL) }

	fun findAnswerBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		dslContext
			.select(
				BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID,
				BOOK_QUIZ_ANSWER.CONTENT,
			).from(BOOK_QUIZ_ANSWER)
			.where(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID.`in`(ids))
			.fetchGroups(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID)
			.mapValues { (_, v) -> v.getValues(BOOK_QUIZ_ANSWER.CONTENT) }
}