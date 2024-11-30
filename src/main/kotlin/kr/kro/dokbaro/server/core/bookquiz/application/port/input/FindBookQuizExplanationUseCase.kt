package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation

fun interface FindBookQuizExplanationUseCase {
	fun findExplanationBy(id: Long): BookQuizExplanation
}