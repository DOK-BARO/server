package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary

@PersistenceAdapter
class BookPersistenceQueryAdapter(
	private val bookRepository: BookQueryRepository,
) : ReadBookCollectionPort,
	ReadBookCategoryPort,
	ReadBookPort {
	override fun getAllBook(
		condition: ReadBookCollectionCondition,
		pagingOption: PagingOption,
	): Collection<BookSummary> = bookRepository.findAllBookBy(condition, pagingOption)

	override fun findTreeBy(id: Long): BookCategoryTree = bookRepository.findAllCategoryBy(id)

	override fun findBy(id: Long): BookDetail? = bookRepository.findById(id)
}