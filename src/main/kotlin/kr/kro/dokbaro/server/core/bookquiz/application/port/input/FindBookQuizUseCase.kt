package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

fun interface FindBookQuizUseCase {
	fun findBy(id: Long): BookQuiz
}