package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions

fun interface FindBookQuizQuestionUseCase {
	fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions
}