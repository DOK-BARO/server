package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import org.jooq.DSLContext
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JStudyGroupQuiz
import org.springframework.stereotype.Repository

@Repository
class BookQuizRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
	}

	fun insert(bookQuiz: BookQuiz): Long {
		val bookQuizId: Long =
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

		bookQuiz.questions.forEach { question ->
			val questionId =
				dslContext
					.insertInto(
						BOOK_QUIZ_QUESTION,
						BOOK_QUIZ_QUESTION.QUESTION_CONTENT,
						BOOK_QUIZ_QUESTION.EXPLANATION,
						BOOK_QUIZ_QUESTION.QUESTION_TYPE,
						BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID,
					).values(
						question.content.toByteArray(),
						question.answerExplanation.toByteArray(),
						question.quizType.name,
						bookQuizId,
					).returningResult(BOOK_QUIZ_QUESTION.ID)
					.fetchOneInto(Long::class.java)!!

			question.answer.getAnswers().forEach {
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

			question.selectOptions
				.map { it.content.toByteArray() }
				.forEachIndexed { index, value ->
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
		}

		bookQuiz.studyGroups.forEach {
			dslContext
				.insertInto(
					STUDY_GROUP_QUIZ,
					STUDY_GROUP_QUIZ.STUDY_GROUP_ID,
					STUDY_GROUP_QUIZ.BOOK_QUIZ_ID,
				).values(
					it,
					bookQuizId,
				)
		}

		return bookQuizId
	}
}