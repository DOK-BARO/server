package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.application.service.exception.BookNotFoundException
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import org.springframework.stereotype.Service

@Service
class BookQueryService(
	private val readBookCollectionPort: ReadBookCollectionPort,
	private val readBookPort: ReadBookPort,
	private val readBookCategoryPort: ReadBookCategoryPort,
) : FindAllBookUseCase,
	FindOneBookUseCase,
	FindAllBookCategoryUseCase {
	override fun findAllBy(command: FindAllBookCommand): Collection<BookSummary> =
		readBookCollectionPort.getAllBook(
			ReadBookCollectionCondition(
				command.title,
				command.authorName,
				command.description,
				command.category,
			),
			PagingOption.of(command.page, command.size),
		)

	override fun getBy(id: Long): BookDetail = readBookPort.findBy(id) ?: throw BookNotFoundException(id)

	override fun getTree(id: Long?): BookCategoryTree = readBookCategoryPort.findTreeBy(id ?: BookCategory.ROOT_ID)
}