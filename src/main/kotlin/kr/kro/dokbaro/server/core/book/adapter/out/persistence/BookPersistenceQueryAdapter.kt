package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadIntegratedBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary

@PersistenceAdapter
class BookPersistenceQueryAdapter(
	private val bookRepository: BookQueryRepository,
) : ReadBookCollectionPort,
	ReadBookPort,
	ReadIntegratedBookCollectionPort {
	override fun getAllBook(
		condition: ReadBookCollectionCondition,
		pageOption: PageOption,
	): Collection<BookSummary> = bookRepository.findAllBookBy(condition, pageOption)

	override fun findBy(id: Long): BookDetail? = bookRepository.findById(id)

	override fun findAllIntegratedBook(
		pageOption: PageOption,
		keyword: String,
	): Collection<BookSummary> = bookRepository.findAllIntegratedBook(pageOption, keyword)
}