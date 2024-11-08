package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizContributor
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JStudyGroupQuiz
import org.jooq.generated.tables.records.SolvingQuizRecord
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

	fun findAllMySolveSummary(memberId: Long): Collection<MySolveSummary> {
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
				.fetch()

		return solvingQuizMapper.toMySolveSummary(record)
	}

	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
	): Collection<StudyGroupSolveSummary> {
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
					creator.ID.`as`(SolvingQuizRecordFieldName.CREATOR_ID.name),
					creator.NICKNAME.`as`(SolvingQuizRecordFieldName.CREATOR_NAME.name),
					creator.PROFILE_IMAGE_URL.`as`(SolvingQuizRecordFieldName.CREATOR_IMAGE_URL.name),
					contributor.ID.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_ID.name),
					contributor.NICKNAME.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_NAME.name),
					contributor.PROFILE_IMAGE_URL.`as`(SolvingQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL.name),
				).from(SOLVING_QUIZ)
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
}