package kr.kro.dokbaro.server.core.bookquiz.query

import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption

data class BookQuizQuestions(
	val id: Long,
	val title: String,
	val timeLimitSecond: Int? = null,
	val questions: Collection<Question>,
)

data class Question(
	val id: Long,
	val content: String,
	val type: QuizType,
	val selectOptions: Collection<SelectOption>,
)