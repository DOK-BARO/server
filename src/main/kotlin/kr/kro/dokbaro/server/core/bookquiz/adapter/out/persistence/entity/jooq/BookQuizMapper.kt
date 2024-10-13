package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import org.jooq.Record9
import org.jooq.Result
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption

@Mapper
class BookQuizMapper {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
	}

	fun recordToBookQuizQuestions(
		record: Result<Record9<Long, String, Int, Long, ByteArray, String, Long, ByteArray, Int>>,
	): BookQuizQuestions? =
		record
			.groupBy {
				Triple(it.get(BOOK_QUIZ.ID), it.get(BOOK_QUIZ.TITLE), it.get(BOOK_QUIZ.TIME_LIMIT_SECOND))
			}.mapValues { entry ->
				entry.value
					.groupBy {
						Triple(
							it.get(BOOK_QUIZ_QUESTION.ID),
							it.get(BOOK_QUIZ_QUESTION.QUESTION_CONTENT),
							it.get(BOOK_QUIZ_QUESTION.QUESTION_TYPE),
						)
					}.map {
						Question(
							it.key.first,
							it.key.second.toString(Charsets.UTF_8),
							QuizType.valueOf(it.key.third),
							it.value.map { v ->
								SelectOption(
									v.get(BOOK_QUIZ_SELECT_OPTION.CONTENT).toString(Charsets.UTF_8),
								)
							},
						)
					}
			}.map {
				BookQuizQuestions(it.key.first, it.key.second, it.key.third, it.value)
			}.firstOrNull()
}