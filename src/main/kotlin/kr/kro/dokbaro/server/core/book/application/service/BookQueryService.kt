package kr.kro.dokbaro.server.core.book.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindIntegratedBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.CountBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadIntegratedBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.application.service.exception.BookNotFoundException
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.BookSummarySortKeyword
import kr.kro.dokbaro.server.core.book.query.IntegratedBook
import org.springframework.stereotype.Service

@Service
class BookQueryService(
	private val readBookCollectionPort: ReadBookCollectionPort,
	private val readBookPort: ReadBookPort,
	private val readIntegratedBookCollectionPort: ReadIntegratedBookCollectionPort,
	private val countBookPort: CountBookPort,
) : FindAllBookUseCase,
	FindOneBookUseCase,
	FindIntegratedBookUseCase {
	override fun findAllBy(
		command: FindAllBookCommand,
		pageOption: PageOption<BookSummarySortKeyword>,
	): PageResponse<BookSummary> {
		val condition =
			ReadBookCollectionCondition(
				title = command.title,
				authorName = command.authorName,
				description = command.description,
				categoryId = command.category,
			)
		val count = countBookPort.countBy(condition)
		val data =
			readBookCollectionPort.getAllBook(
				condition = condition,
				pageOption,
			)

		return PageResponse.of(
			totalElementCount = count,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun getBy(id: Long): BookDetail = readBookPort.findBy(id) ?: throw BookNotFoundException(id)

	override fun findAllIntegratedBooks(
		size: Long,
		keyword: String,
		lastId: Long?,
	): Collection<IntegratedBook> = readIntegratedBookCollectionPort.findAllIntegratedBook(size, keyword, lastId)
}