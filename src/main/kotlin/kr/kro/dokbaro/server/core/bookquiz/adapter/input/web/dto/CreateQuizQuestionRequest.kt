package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class CreateQuizQuestionRequest(
	val content: String,
	val selectOptions: Collection<String> = emptyList(),
	val answerExplanation: String,
	val answerType: QuizType,
	val answers: Collection<String> = emptyList(),
)