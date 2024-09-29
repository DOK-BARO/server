package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import org.jooq.DSLContext
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.springframework.stereotype.Repository

@Repository
class QuizQuestionRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
	}

	fun insert(quizQuestion: QuizQuestion): Long {
		val questionId =
			dslContext
				.insertInto(
					BOOK_QUIZ_QUESTION,
					BOOK_QUIZ_QUESTION.QUESTION_CONTENT,
					BOOK_QUIZ_QUESTION.EXPLANATION,
					BOOK_QUIZ_QUESTION.QUESTION_TYPE,
					BOOK_QUIZ_QUESTION.BOOK_QUIZ_ID,
				).values(
					quizQuestion.content.toByteArray(),
					quizQuestion.answerExplanation.toByteArray(),
					quizQuestion.quizType.name,
					quizQuestion.quizId,
				).returningResult(BOOK_QUIZ_QUESTION.ID)
				.fetchOneInto(Long::class.java)!!

		quizQuestion.answer.getAnswers().forEach {
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

		quizQuestion.selectOptions
			.map { it.content.toByteArray() }
			.forEach {
				dslContext
					.insertInto(
						BOOK_QUIZ_SELECT_OPTION,
						BOOK_QUIZ_SELECT_OPTION.BOOK_QUIZ_QUESTION_ID,
						BOOK_QUIZ_SELECT_OPTION.CONTENT,
					).values(
						questionId,
						it,
					)
			}

		return questionId
	}
}