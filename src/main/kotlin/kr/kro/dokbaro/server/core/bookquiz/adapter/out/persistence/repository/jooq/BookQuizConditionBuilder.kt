package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.query.condition.MyBookQuizSummaryFilterCondition
import org.jooq.Condition
import org.jooq.generated.tables.JBookQuiz.BOOK_QUIZ
import org.jooq.generated.tables.JSolvingQuiz.SOLVING_QUIZ
import org.jooq.generated.tables.JStudyGroupQuiz.STUDY_GROUP_QUIZ
import org.jooq.impl.DSL
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Component

@Component
class BookQuizConditionBuilder {
	fun buildCountCondition(condition: CountBookQuizCondition): Condition =
		DSL.and(
			condition.bookId?.let { BOOK_QUIZ.BOOK_ID.eq(it) },
			condition.creatorId?.let { BOOK_QUIZ.CREATOR_ID.eq(it) },
			if (condition.studyGroup.only) {
				BOOK_QUIZ.ID
					.`in`(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ),
					)
			} else {
				DSL.trueCondition()
			},
			if (condition.studyGroup.exclude) {
				BOOK_QUIZ.ID
					.notIn(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ),
					)
			} else {
				DSL.trueCondition()
			},
			condition.studyGroup.id?.let {
				BOOK_QUIZ.ID
					.`in`(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ)
							.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(it)),
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
			BOOK_QUIZ.TEMPORARY.eq(condition.temporary),
			condition.viewScope?.let { BOOK_QUIZ.VIEW_SCOPE.eq(it.name) },
		)

	fun buildMyBookQuizCondition(condition: MyBookQuizSummaryFilterCondition): Condition =
		DSL.and(
			if (condition.studyGroup.only) {
				BOOK_QUIZ.ID
					.`in`(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ),
					)
			} else {
				DSL.trueCondition()
			},
			if (condition.studyGroup.exclude) {
				BOOK_QUIZ.ID
					.notIn(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ),
					)
			} else {
				DSL.trueCondition()
			},
			condition.studyGroup.id?.let {
				BOOK_QUIZ.ID
					.`in`(
						select(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID)
							.from(STUDY_GROUP_QUIZ)
							.where(STUDY_GROUP_QUIZ.STUDY_GROUP_ID.eq(it)),
					)
			},
			BOOK_QUIZ.TEMPORARY.eq(condition.temporary),
			condition.viewScope?.let { BOOK_QUIZ.VIEW_SCOPE.eq(it.name) },
		)
}