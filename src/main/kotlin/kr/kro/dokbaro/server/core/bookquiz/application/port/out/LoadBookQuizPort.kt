package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

fun interface LoadBookQuizPort {
	fun load(id: Long): BookQuiz?
}