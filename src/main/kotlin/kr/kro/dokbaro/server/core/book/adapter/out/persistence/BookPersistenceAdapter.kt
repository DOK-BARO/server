package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookCategory

@PersistenceAdapter
class BookPersistenceAdapter(
	private val queryRepository: BookQueryRepository,
) : ReadBookCollectionPort,
	ReadBookCategoryPort,
	LoadBookPort {
	override fun getAllBook(
		condition: ReadBookCollectionCondition,
		pagingOption: PagingOption,
	): Collection<Book> = queryRepository.findAllBookBy(condition, pagingOption)

	override fun getBookCategory(id: Long): BookCategory = queryRepository.findAllCategoryBy(id)

	override fun getBook(id: Long): Book? = queryRepository.findById(id)
}