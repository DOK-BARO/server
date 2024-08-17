package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCategoryPort
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.BookCollectionPagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.LoadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookCategory

@PersistenceAdapter
class BookPersistenceAdapter(
	private val queryRepository: BookQueryRepository,
) : LoadBookCollectionPort,
	LoadBookCategoryPort,
	LoadBookPort {
	override fun getAllBook(
		condition: LoadBookCollectionCondition,
		pagingOption: BookCollectionPagingOption,
	): Collection<Book> = queryRepository.findAllBookBy(condition, pagingOption)

	override fun getBookCategory(id: Long): BookCategory = queryRepository.findAllCategoryBy(id)

	override fun getBook(id: Long): Book? = queryRepository.findById(id)
}