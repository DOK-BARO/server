package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree

@PersistenceAdapter
class BookCategoryPersistenceQueryAdapter(
	private val bookRepository: BookQueryRepository,
) : ReadBookCategoryPort {
	override fun findTreeBy(id: Long): BookCategoryTree = bookRepository.findAllCategoryBy(id)
}