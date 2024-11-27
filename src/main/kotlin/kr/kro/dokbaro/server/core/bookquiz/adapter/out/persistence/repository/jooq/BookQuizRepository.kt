package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizContributor
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JStudyGroupQuiz
import org.jooq.generated.tables.records.BookQuizRecord
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Repository

@Repository
class BookQuizRepository(
	private val dslContext: DSLContext,
	private val bookQuizMapper: BookQuizMapper,
) {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
		private val BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE = JBookQuizAnswerExplainImage.BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE
		private val BOOK_QUIZ_CONTRIBUTOR = JBookQuizContributor.BOOK_QUIZ_CONTRIBUTOR
	}

	fun insert(bookQuiz: BookQuiz): Long {
		val bookQuizId: Long =
			insertBookQuiz(bookQuiz)

		bookQuiz.questions.getQuestions().forEach { question ->
			insertBookQuizQuestion(question, bookQuizId)
		}

		insertStudyGroupQuiz(bookQuiz, bookQuizId)

		val contributorInsertQuery =
			bookQuiz.contributorIds.map { contributorId ->
				dslContext
					.insertInto(
						BOOK_QUIZ_CONTRIBUTOR,
						BOOK_QUIZ_CONTRIBUTOR.BOOK_QUIZ_ID,
						BOOK_QUIZ_CONTRIBUTOR.MEMBER_ID,
					).values(
						bookQuizId,
						contributorId,
					)
			}

		dslContext.batch(contributorInsertQuery).execute()

		return bookQuizId
	}

	private fun insertBookQuiz(bookQuiz: BookQuiz) =
		dslContext
			.insertInto(
				BOOK_QUIZ,
				BOOK_QUIZ.TITLE,
				BOOK_QUIZ.DESCRIPTION,
				BOOK_QUIZ.CREATOR_ID,
				BOOK_QUIZ.BOOK_ID,
				BOOK_QUIZ.TIME_LIMIT_SECOND,
				BOOK_QUIZ.VIEW_SCOPE,
				BOOK_QUIZ.EDIT_SCOPE,
			).values(
				bookQuiz.title,
				bookQuiz.description.toByteArray(),
				bookQuiz.creatorId,
				bookQuiz.bookId,
				bookQuiz.timeLimitSecond,
				bookQuiz.viewScope.name,
				bookQuiz.editScope.name,
			).returningResult(BOOK_QUIZ.ID)
			.fetchOneInto(Long::class.java)!!

	private fun insertBookQuizQuestion(
		question: QuizQuestion,
		bookQuizId: Long,
	) {
		val questionId =
			dslContext
				.insertInto(
					BOOK_QUIZ_QUESTION,
					BOOK_QUIZ_QUESTION.QUESTION_CONTENT,
					BOOK_QUIZ_QUESTION.EXPLANATION,
					BOOK_QUIZ_QUESTION.QUESTION_TYPE,
					BOOK_QUIZ_QUESTION.ACTIVE,
					BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID,
				).values(
					question.content.toByteArray(),
					question.answer.explanationContent.toByteArray(),
					question.quizType.name,
					question.active,
					bookQuizId,
				).returningResult(BOOK_QUIZ_QUESTION.ID)
				.fetchOneInto(Long::class.java)!!

		insertQuestionAnswerAndOptions(question, questionId)
	}

	private fun insertQuestionAnswerAndOptions(
		question: QuizQuestion,
		questionId: Long,
	) {
		val answerInsertQuery =
			question.answer.gradeSheet.getAnswers().map {
				dslContext
					.insertInto(
						BOOK_QUIZ_ANSWER,
						BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID,
						BOOK_QUIZ_ANSWER.CONTENT,
					).values(
						questionId,
						it,
					)
			}

		dslContext.batch(answerInsertQuery).execute()

		val optionInsertQuery =
			question.selectOptions
				.map { it.content.toByteArray() }
				.mapIndexed { index, value ->
					dslContext
						.insertInto(
							BOOK_QUIZ_SELECT_OPTION,
							BOOK_QUIZ_SELECT_OPTION.SEQ,
							BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID,
							BOOK_QUIZ_SELECT_OPTION.CONTENT,
						).values(
							index,
							questionId,
							value,
						)
				}
		dslContext.batch(optionInsertQuery).execute()

		val explanationInsertQuery =
			question.answer.explanationImages.map {
				dslContext
					.insertInto(
						BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE,
						BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID,
						BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL,
					).values(
						questionId,
						it,
					)
			}

		dslContext.batch(explanationInsertQuery).execute()
	}

	fun load(id: Long): BookQuiz? {
		val record: Map<BookQuizRecord, Result<Record>> =
			dslContext
				.select()
				.from(BOOK_QUIZ)
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID).and(BOOK_QUIZ_QUESTION.DELETED.eq(false)))
				.leftJoin(BOOK_QUIZ_SELECT_OPTION)
				.on(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.leftJoin(BOOK_QUIZ_ANSWER)
				.on(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.leftJoin(STUDY_GROUP_QUIZ)
				.on(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(STUDY_GROUP_QUIZ.ID))
				.leftJoin(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE)
				.on(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.where(BOOK_QUIZ.ID.eq(id).and(BOOK_QUIZ.DELETED.eq(false)))
				.fetchGroups(BOOK_QUIZ)

		return bookQuizMapper.toBookQuiz(record)
	}

	fun update(bookQuiz: BookQuiz) {
		dslContext
			.update(BOOK_QUIZ)
			.set(BOOK_QUIZ.TITLE, bookQuiz.title)
			.set(BOOK_QUIZ.DESCRIPTION, bookQuiz.description.toByteArray())
			.set(BOOK_QUIZ.BOOK_ID, bookQuiz.bookId)
			.set(BOOK_QUIZ.TIME_LIMIT_SECOND, bookQuiz.timeLimitSecond)
			.set(BOOK_QUIZ.VIEW_SCOPE, bookQuiz.viewScope.name)
			.set(BOOK_QUIZ.EDIT_SCOPE, bookQuiz.editScope.name)
			.where(BOOK_QUIZ.ID.eq(bookQuiz.id))
			.execute()

		dslContext
			.deleteFrom(STUDY_GROUP_QUIZ)
			.where(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(bookQuiz.id))
			.execute()

		insertStudyGroupQuiz(bookQuiz, bookQuiz.id)

		dslContext
			.update(BOOK_QUIZ_QUESTION)
			.set(BOOK_QUIZ_QUESTION.DELETED, true)
			.where(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(bookQuiz.id))
			.execute()

		bookQuiz.questions.getQuestions().forEach { question ->
			if (question.id == Constants.UNSAVED_ID) {
				insertBookQuizQuestion(question, bookQuiz.id)
			} else {
				updateBookQuizQuestion(question, bookQuiz.id)
			}
		}

		dslContext
			.deleteFrom(BOOK_QUIZ_CONTRIBUTOR)
			.where(BOOK_QUIZ_CONTRIBUTOR.BOOK_QUIZ_ID.eq(bookQuiz.id))
			.execute()

		val contributorInsertQuery =
			bookQuiz.contributorIds.map { contributorId ->
				dslContext
					.insertInto(
						BOOK_QUIZ_CONTRIBUTOR,
						BOOK_QUIZ_CONTRIBUTOR.BOOK_QUIZ_ID,
						BOOK_QUIZ_CONTRIBUTOR.MEMBER_ID,
					).values(
						bookQuiz.id,
						contributorId,
					)
			}

		dslContext.batch(contributorInsertQuery).execute()
	}

	private fun updateBookQuizQuestion(
		question: QuizQuestion,
		quizId: Long,
	) {
		dslContext
			.update(BOOK_QUIZ_QUESTION)
			.set(BOOK_QUIZ_QUESTION.DELETED, false)
			.set(BOOK_QUIZ_QUESTION.QUESTION_CONTENT, question.content.toByteArray())
			.set(BOOK_QUIZ_QUESTION.EXPLANATION, question.answer.explanationContent.toByteArray())
			.set(BOOK_QUIZ_QUESTION.QUESTION_TYPE, question.quizType.name)
			.set(BOOK_QUIZ_QUESTION.ACTIVE, question.active)
			.set(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID, quizId)
			.where(BOOK_QUIZ_QUESTION.ID.eq(question.id))
			.execute()

		dslContext
			.deleteFrom(BOOK_QUIZ_ANSWER)
			.where(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID.eq(question.id))
			.execute()
		dslContext
			.deleteFrom(BOOK_QUIZ_SELECT_OPTION)
			.where(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.eq(question.id))
			.execute()
		dslContext
			.deleteFrom(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE)
			.where(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID.eq(question.id))
			.execute()

		insertQuestionAnswerAndOptions(question, question.id)
	}

	private fun insertStudyGroupQuiz(
		bookQuiz: BookQuiz,
		bookQuizId: Long,
	) {
		bookQuiz.studyGroupId?.let {
			dslContext
				.insertInto(
					STUDY_GROUP_QUIZ,
					STUDY_GROUP_QUIZ.STUDY_GROUP_ID,
					STUDY_GROUP_QUIZ.BOOK_QUIZ_ID,
				).values(
					it,
					bookQuizId,
				).execute()
		}
	}

	fun loadByQuestionId(questionId: Long): BookQuiz? {
		val record: Map<BookQuizRecord, Result<Record>> =
			dslContext
				.select()
				.from(BOOK_QUIZ)
				.join(BOOK_QUIZ_QUESTION)
				.on(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID.eq(BOOK_QUIZ.ID).and(BOOK_QUIZ_QUESTION.DELETED.eq(false)))
				.leftJoin(BOOK_QUIZ_SELECT_OPTION)
				.on(BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.leftJoin(BOOK_QUIZ_ANSWER)
				.on(BOOK_QUIZ_ANSWER.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.leftJoin(STUDY_GROUP_QUIZ)
				.on(STUDY_GROUP_QUIZ.BOOK_QUIZ_ID.eq(STUDY_GROUP_QUIZ.ID))
				.leftJoin(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE)
				.on(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.BOOK_QUIZ_QUESTION_ID.eq(BOOK_QUIZ_QUESTION.ID))
				.where(
					BOOK_QUIZ.ID
						.eq(
							select(BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID).from(BOOK_QUIZ_QUESTION).where(
								BOOK_QUIZ_QUESTION.ID.eq(questionId),
							),
						).and(BOOK_QUIZ.DELETED.eq(false)),
				).fetchGroups(BOOK_QUIZ)

		return bookQuizMapper.toBookQuiz(record)
	}
}