package kr.kro.dokbaro.server.core.book.application.port.input.query

import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.domain.Book

fun interface FindAllBookUseCase {
	fun findBy(command: FindAllBookCommand): Collection<Book>
}