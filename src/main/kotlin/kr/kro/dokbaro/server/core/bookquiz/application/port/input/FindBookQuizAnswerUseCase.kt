package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer

fun interface FindBookQuizAnswerUseCase {
	fun findBookQuizAnswer(questionId: Long): BookQuizAnswer
}