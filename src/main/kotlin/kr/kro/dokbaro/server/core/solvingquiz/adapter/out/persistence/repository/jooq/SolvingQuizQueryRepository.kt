package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.OrderField
import org.jooq.Record
import org.jooq.Record1
import org.jooq.Result
import org.jooq.Table
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizContributor
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JStudyGroupQuiz
import org.jooq.generated.tables.records.SolvingQuizRecord
import org.jooq.impl.DSL
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Repository

@Repository
class SolvingQuizQueryRepository(
	private val dslContext: DSLContext,
	private val solvingQuizMapper: SolvingQuizMapper,
) {
	companion object {
		private val SOLVING_QUIZ = JSolvingQuiz.SOLVING_QUIZ
		private val BOOK = JBook.BOOK
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val MEMBER = JMember.MEMBER
		private val BOOK_QUIZ_CONTRIBUTOR = JBookQuizContributor.BOOK_QUIZ_CONTRIBUTOR
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
	}

	fun findAllMySolveSummary(
		memberId: Long,
		pageOption: PageOption<MySolvingQuizSortKeyword>,
	): Collection<MySolveSummary> {
		val record: Result<out Record> =
			dslContext
				.select(
					SOLVING_QUIZ.ID,
					SOLVING_QUIZ.CREATED_AT,
					BOOK.IMAGE_URL,
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
				).from(SOLVING_QUIZ)
				.join(BOOK_QUIZ)
				.on(BOOK_QUIZ.ID.eq(SOLVING_QUIZ.QUIZ_ID))
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.where(SOLVING_QUIZ.MEMBER_ID.eq(memberId))
				.orderBy(toMySolvingQuizOrderQuery(pageOption), SOLVING_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.fetch()

		return solvingQuizMapper.toMySolveSummary(record)
	}

	private fun toMySolvingQuizOrderQuery(pageOption: PageOption<MySolvingQuizSortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				MySolvingQuizSortKeyword.TITLE -> BOOK_QUIZ.TITLE
				MySolvingQuizSortKeyword.CREATED_AT -> SOLVING_QUIZ.CREATED_AT
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>,
	): Collection<StudyGroupSolveSummary> {
		val pageSolvingQuiz: Table<Record1<Long>> =
			dslContext
				.select(SOLVING_QUIZ.ID)
				.from(SOLVING_QUIZ)
				.join(BOOK_QUIZ)
				.on(BOOK_QUIZ.ID.eq(SOLVING_QUIZ.QUIZ_ID))
				.where(
					SOLVING_QUIZ.QUIZ_ID
						.`in`(
							select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
								.from(STUDY_GROUP_QUIZ)
								.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(studyGroupId)),
						).and(SOLVING_QUIZ.MEMBER_ID.eq(memberId)),
				).orderBy(toOrderQuery(pageOption), SOLVING_QUIZ.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.asTable("paged_solving_quiz")

		val creator = MEMBER.`as`("CREATOR")
		val contributor = MEMBER.`as`("CONTRIBUTOR")

		val record: Map<SolvingQuizRecord, Result<out Record>> =
			dslContext
				.select(
					SOLVING_QUIZ.ID,
					SOLVING_QUIZ.CREATED_AT,
					BOOK.ID,
					BOOK.TITLE,
					BOOK.IMAGE_URL,
					BOOK_QUIZ.ID,
					BOOK_QUIZ.TITLE,
					BOOK_QUIZ.CREATED_AT,
					creator.ID.`as`(SolvingQuizRecordFieldName.CREATOR_ID),
					creator.NICKNAME.`as`(SolvingQuizRecordFieldName.CREATOR_NAME),
					creator.PROFILE_IMAGE_URL.`as`(SolvingQuizRecordFieldName.CREATOR_IMAGE_URL),
					contributor.ID.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_ID),
					contributor.NICKNAME.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_NAME),
					contributor.PROFILE_IMAGE_URL.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL),
				).from(pageSolvingQuiz)
				.join(SOLVING_QUIZ)
				.on(SOLVING_QUIZ.ID.eq(pageSolvingQuiz.field(SOLVING_QUIZ.ID)))
				.join(BOOK_QUIZ)
				.on(BOOK_QUIZ.ID.eq(SOLVING_QUIZ.QUIZ_ID))
				.join(BOOK)
				.on(BOOK.ID.eq(BOOK_QUIZ.BOOK_ID))
				.join(creator)
				.on(creator.ID.eq(BOOK_QUIZ.CREATOR_ID))
				.leftJoin(BOOK_QUIZ_CONTRIBUTOR)
				.on(BOOK_QUIZ_CONTRIBUTOR.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID))
				.leftJoin(contributor)
				.on(contributor.ID.eq(BOOK_QUIZ_CONTRIBUTOR.MEMBER_ID))
				.where(
					SOLVING_QUIZ.QUIZ_ID
						.`in`(
							select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
								.from(STUDY_GROUP_QUIZ)
								.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(studyGroupId)),
						).and(SOLVING_QUIZ.MEMBER_ID.eq(memberId)),
				).fetchGroups(SOLVING_QUIZ)

		return solvingQuizMapper.toMyStudyGroupSolveSummary(record)
	}

	private fun toOrderQuery(pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				MyStudyGroupSolveSummarySortKeyword.CREATED_AT -> BOOK_QUIZ.CREATED_AT
				MyStudyGroupSolveSummarySortKeyword.UPDATED_AT -> BOOK_QUIZ.UPDATED_AT
				MyStudyGroupSolveSummarySortKeyword.TITLE -> BOOK_QUIZ.TITLE
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	fun countBy(condition: CountSolvingQuizCondition): Long =
		dslContext
			.selectCount()
			.from(SOLVING_QUIZ)
			.where(buildCountCondition(condition).and(SOLVING_QUIZ.DELETED.eq(false)))
			.fetchOneInto(Long::class.java)!!

	private fun buildCountCondition(condition: CountSolvingQuizCondition): Condition =
		DSL.and(
			condition.memberId?.let { SOLVING_QUIZ.MEMBER_ID.eq(it) },
			condition.studyGroupId?.let {
				SOLVING_QUIZ.QUIZ_ID.`in`(
					select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
						.from(STUDY_GROUP_QUIZ)
						.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(it)),
				)
			},
		)
}