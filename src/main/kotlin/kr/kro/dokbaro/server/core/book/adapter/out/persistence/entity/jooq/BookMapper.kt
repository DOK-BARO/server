package kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategory
import org.jooq.generated.tables.JBookCategoryGroup
import org.jooq.generated.tables.records.BookCategoryRecord
import org.jooq.generated.tables.records.BookRecord
import org.springframework.stereotype.Component

@Component
class BookMapper {
	companion object {
		private val BOOK = JBook.BOOK
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
		private val BOOK_CATEGORY = JBookCategory.BOOK_CATEGORY
		private val BOOK_CATEGORY_GROUP = JBookCategoryGroup.BOOK_CATEGORY_GROUP
	}

	fun mapToBookCollection(record: Map<BookRecord, Result<Record>>): Collection<Book> = mapRecordMapToBook(record)

	fun mapToCategory(
		record: Collection<BookCategoryRecord>,
		headId: Long,
	): BookCategory {
		val categoryMap: Map<Long, MutableSet<Long>> =
			record
				.map { it.id }
				.associateWith { mutableSetOf<Long>() }
				.toMutableMap()

		val recordMap: Map<Long, BookCategoryRecord> =
			record
				.associateBy { it.id }
				.toMutableMap()

		record.forEach {
			categoryMap[it.parentId]?.add(it.id)
		}

		return generateCategoryWithDetails(categoryMap, recordMap, headId)
	}

	private fun generateCategoryWithDetails(
		categoryMap: Map<Long, Set<Long>>,
		recordMap: Map<Long, BookCategoryRecord>,
		headId: Long,
	): BookCategory {
		val targetRecord: BookCategoryRecord = recordMap[headId]!!

		return BookCategory(
			targetRecord.id,
			targetRecord.koreanName,
			categoryMap[headId]!!
				.map { generateCategoryWithDetails(categoryMap, recordMap, it) },
		)
	}

	fun mapToBook(record: Map<BookRecord, Result<Record>>): Book? = mapRecordMapToBook(record).firstOrNull()

	private fun mapRecordMapToBook(record: Map<BookRecord, Result<Record>>) =
		record.map {
			Book(
				it.key.isbn,
				it.key.title,
				it.key.publisher,
				it.key.publishedAt,
				it.key.price,
				it.key.description.toString(Charsets.UTF_8),
				it.key.imageUrl,
				it.value
					.map { v ->
						BookCategory(v.getValue(BOOK_CATEGORY.ID), v.getValue(BOOK_CATEGORY.KOREAN_NAME))
					}.distinct()
					.toSet(),
				it.value.map { v -> BookAuthor(v.getValue(BOOK_AUTHOR.NAME)) }.distinct(),
				it.key.id,
			)
		}
}