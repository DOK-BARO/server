package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookPort
import kr.kro.dokbaro.server.core.book.domain.Book

@PersistenceAdapter
class BookPersistenceAdapter(
	private val bookRepository: BookRepository,
) : InsertBookPort {
	override fun insert(book: Book): Long = bookRepository.insertBook(book)
}