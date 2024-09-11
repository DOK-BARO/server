package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import org.springframework.stereotype.Service

@Service
class BookService(
	private val readBookCollectionPort: ReadBookCollectionPort,
	private val loadBookPort: LoadBookPort,
) : FindAllBookUseCase,
	FindOneBookUseCase {
	override fun findAllBy(command: FindAllBookCommand): Collection<Book> =
		readBookCollectionPort.getAllBook(
			ReadBookCollectionCondition(
				command.title,
				command.authorName,
				command.description,
				command.category,
			),
			PagingOption.of(command.page, command.size),
		)

	override fun findById(id: Long): Book = loadBookPort.getBook(id) ?: throw BookNotFoundException(id)
}