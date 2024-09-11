package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCategoryPort
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookCategory

@PersistenceAdapter
class BookPersistenceAdapter(
	private val bookRepository: BookRepository,
) : ReadBookCollectionPort,
	LoadBookCategoryPort,
	LoadBookPort,
	InsertBookPort {
	override fun getAllBook(
		condition: ReadBookCollectionCondition,
		pagingOption: PagingOption,
	): Collection<Book> = bookRepository.findAllBookBy(condition, pagingOption)

	override fun getBookCategory(id: Long): BookCategory = bookRepository.findAllCategoryBy(id)

	override fun getBook(id: Long): Book? = bookRepository.findById(id)

	override fun insert(book: Book): Long = bookRepository.insert(book)
}