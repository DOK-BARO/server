package kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.book.domain.Book
import org.jooq.DSLContext
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategoryGroup
import org.springframework.stereotype.Repository

@Repository
class BookRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val BOOK = JBook.BOOK
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
		private val BOOK_CATEGORY_GROUP = JBookCategoryGroup.BOOK_CATEGORY_GROUP
	}

	fun insert(book: Book): Long {
		val savedId: Long =
			dslContext
				.insertInto(
					BOOK,
					BOOK.ISBN,
					BOOK.TITLE,
					BOOK.PUBLISHER,
					BOOK.PUBLISHED_AT,
					BOOK.DESCRIPTION,
					BOOK.IMAGE_URL,
				).values(
					book.isbn,
					book.title,
					book.publisher,
					book.publishedAt,
					book.description?.toByteArray(),
					book.imageUrl,
				).returningResult(BOOK.ID)
				.fetchOneInto(Long::class.java)!!

		book.authors.forEach {
			dslContext
				.insertInto(
					BOOK_AUTHOR,
					BOOK_AUTHOR.BOOK_ID,
					BOOK_AUTHOR.NAME,
				).values(
					savedId,
					it.name,
				).execute()
		}

		book.categories.forEach {
			dslContext
				.insertInto(
					BOOK_CATEGORY_GROUP,
					BOOK_CATEGORY_GROUP.BOOK_ID,
					BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID,
				).values(savedId, it)
				.execute()
		}

		return savedId
	}
}