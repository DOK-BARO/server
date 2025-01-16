package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizDetail

fun interface FindBookQuizDetailUseCase {
	fun findBookQuizDetailBy(id: Long): BookQuizDetail
}