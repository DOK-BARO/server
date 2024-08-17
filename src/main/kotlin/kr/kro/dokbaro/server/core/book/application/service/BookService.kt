package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.BookCollectionPagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.LoadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import org.springframework.stereotype.Service

@Service
class BookService(
	private val loadBookCollectionPort: LoadBookCollectionPort,
) : FindAllBookUseCase {
	override fun findBy(command: FindAllBookCommand): Collection<Book> =
		loadBookCollectionPort.getAllBook(
			LoadBookCollectionCondition(
				command.title,
				command.authorName,
				command.description,
				command.category,
			),
			BookCollectionPagingOption(
				command.lastId,
				command.limit,
			),
		)
}