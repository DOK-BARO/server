package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.dto.BookCollectionPagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.LoadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book

@PersistenceAdapter
class BookPersistenceAdapter(
	private val queryRepository: BookQueryRepository,
) : LoadBookCollectionPort {
	override fun getAll(
		condition: LoadBookCollectionCondition,
		pagingOption: BookCollectionPagingOption,
	): Collection<Book> = queryRepository.findAllBy(condition, pagingOption)
}