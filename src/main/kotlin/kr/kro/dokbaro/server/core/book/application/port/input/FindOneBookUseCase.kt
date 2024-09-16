package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.query.BookDetail

fun interface FindOneBookUseCase {
	fun getBy(id: Long): BookDetail
}