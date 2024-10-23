package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.CountBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadIntegratedBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.BookSummarySortOption
import kr.kro.dokbaro.server.core.book.query.IntegratedBook

@PersistenceAdapter
class BookPersistenceQueryAdapter(
	private val bookRepository: BookQueryRepository,
) : ReadBookCollectionPort,
	ReadBookPort,
	ReadIntegratedBookCollectionPort,
	CountBookPort {
	override fun getAllBook(
		condition: ReadBookCollectionCondition,
		pageOption: PageOption,
		sortOption: SortOption<BookSummarySortOption>,
	): Collection<BookSummary> = bookRepository.findAllBookBy(condition, pageOption, sortOption)

	override fun findBy(id: Long): BookDetail? = bookRepository.findById(id)

	override fun findAllIntegratedBook(
		pageOption: PageOption,
		keyword: String,
	): Collection<IntegratedBook> = bookRepository.findAllIntegratedBook(pageOption, keyword)

	override fun countBy(condition: ReadBookCollectionCondition): Long = bookRepository.countBy(condition)
}