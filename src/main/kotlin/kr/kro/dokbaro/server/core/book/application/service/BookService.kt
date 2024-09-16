package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookPort
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import org.springframework.stereotype.Service

@Service
class BookService(
	private val insertBookPort: InsertBookPort,
) : CreateBookUseCase {
	override fun create(command: CreateBookCommand): Long =
		insertBookPort.insert(
			Book(
				isbn = command.isbn,
				title = command.title,
				publisher = command.publisher,
				publishedAt = command.publishedAt,
				price = command.price,
				description = command.description,
				imageUrl = command.imageUrl,
				categories = command.categories,
				authors = command.authors.map { BookAuthor(it) },
			),
		)
}