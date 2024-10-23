package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import org.jooq.DSLContext
import org.jooq.Record3
import org.jooq.Record9
import org.jooq.Result
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
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
}