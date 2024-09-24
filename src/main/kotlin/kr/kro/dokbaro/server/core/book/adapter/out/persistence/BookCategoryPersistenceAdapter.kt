package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory

@PersistenceAdapter
class BookCategoryPersistenceAdapter(
	private val bookRepository: BookRepository,
) : InsertBookCategoryPort {
	override fun insert(bookCategory: BookCategory): Long = bookRepository.insertBookCategory(bookCategory)
}