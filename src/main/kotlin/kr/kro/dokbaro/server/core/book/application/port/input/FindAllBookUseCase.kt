package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.query.BookSummary

fun interface FindAllBookUseCase {
	fun findAllBy(command: FindAllBookCommand): Collection<BookSummary>
}