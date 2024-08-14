package kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategory
import org.jooq.generated.tables.records.BookRecord
import org.springframework.stereotype.Component

@Component
class BookMapper {
	companion object {
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
		private val BOOK_CATEGORY = JBookCategory.BOOK_CATEGORY
	}

	fun mapToCollection(record: Map<BookRecord, Result<Record>>): Collection<Book> =
		record.map {
			Book(
				it.key.isbn,
				it.key.title,
				it.key.publisher,
				it.key.publishedAt,
				it.key.price,
				it.key.description.toString(Charsets.UTF_8),
				it.key.imageUrl,
				it.value.map { v -> BookCategory(v.getValue(BOOK_CATEGORY.ID), v.getValue(BOOK_CATEGORY.KOREAN_NAME)) },
				it.value.map { v -> BookAuthor(v.getValue(BOOK_AUTHOR.NAME)) },
				it.key.id,
			)
		}
}