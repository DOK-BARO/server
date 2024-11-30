package kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.jooq.DSLContext
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategory
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
		private val BOOK_CATEGORY = JBookCategory.BOOK_CATEGORY
	}

	fun insertBook(book: Book): Long {
		val savedId: Long =
			dslContext
				.insertInto(
					BOOK,
					BOOK.ISBN,
					BOOK.TITLE,
					BOOK.PUBLISHER,
					BOOK.PUBLISHED_AT,
					BOOK.PRICE,
					BOOK.DESCRIPTION,
					BOOK.IMAGE_URL,
				).values(
					book.isbn,
					book.title,
					book.publisher,
					book.publishedAt,
					book.price,
					book.description,
					book.imageUrl,
				).returningResult(BOOK.ID)
				.fetchOneInto(Long::class.java)!!

		val insertAuthorQuery =
			book.authors.map {
				dslContext
					.insertInto(
						BOOK_AUTHOR,
						BOOK_AUTHOR.BOOK_ID,
						BOOK_AUTHOR.NAME,
					).values(
						savedId,
						it.name,
					)
			}
		dslContext.batch(insertAuthorQuery).execute()

		val insertCategoryQuery =
			book.categories.map {
				dslContext
					.insertInto(
						BOOK_CATEGORY_GROUP,
						BOOK_CATEGORY_GROUP.BOOK_ID,
						BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID,
					).values(savedId, it)
			}

		dslContext.batch(insertCategoryQuery).execute()

		return savedId
	}

	fun insertBookCategory(bookCategory: BookCategory): Long =
		dslContext
			.insertInto(
				BOOK_CATEGORY,
				BOOK_CATEGORY.KOREAN_NAME,
				BOOK_CATEGORY.ENGLISH_NAME,
				BOOK_CATEGORY.PARENT_ID,
			).values(
				bookCategory.koreanName,
				bookCategory.englishName,
				bookCategory.parentId,
			).returningResult(BOOK_CATEGORY.ID)
			.fetchOneInto(Long::class.java)!!
}