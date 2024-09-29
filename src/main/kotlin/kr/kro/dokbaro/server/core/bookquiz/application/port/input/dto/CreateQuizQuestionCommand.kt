package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class CreateQuizQuestionCommand(
	val quizId: Long,
	val content: String,
	val selectOptions: Collection<String> = emptyList(),
	val answerExplanation: String,
	val answerType: QuizType,
	val answers: Collection<String> = emptyList(),
)