package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.domain.Book

fun interface FindOneBookUseCase {
	fun findById(id: Long): Book
}