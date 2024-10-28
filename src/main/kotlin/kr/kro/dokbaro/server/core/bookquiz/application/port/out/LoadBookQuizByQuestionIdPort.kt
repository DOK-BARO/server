package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

fun interface LoadBookQuizByQuestionIdPort {
	fun loadByQuestionId(questionId: Long): BookQuiz?
}