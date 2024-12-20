package kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRecordFieldName
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookCategorySingle
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.IntegratedBook
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.records.BookCategoryRecord

@Mapper
class BookMapper {
	companion object {
		private val BOOK = JBook.BOOK
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
	}

	fun toSummaryCollection(record: Map<Long, Result<out Record>>): Collection<BookSummary> =
		record.map {
			BookSummary(
				id = it.key,
				title = it.value.getValues(BOOK.TITLE).first(),
				publisher = it.value.getValues(BOOK.PUBLISHER).first(),
				imageUrl = it.value.getValues(BOOK.IMAGE_URL).firstOrNull(),
				authors = it.value.getValues(BOOK_AUTHOR.NAME).distinct(),
				quizCount = it.value.getValues(BookRecordFieldName.QUIZ_COUNT, Long::class.java).first(),
			)
		}

	fun toIntegratedBookCollection(record: Map<Long, Result<out Record>>): Collection<IntegratedBook> =
		record.map {
			IntegratedBook(
				id = it.key,
				title = it.value.getValues(BOOK.TITLE).first(),
				publisher = it.value.getValues(BOOK.PUBLISHER).first(),
				imageUrl = it.value.getValues(BOOK.IMAGE_URL).firstOrNull(),
				authors = it.value.getValues(BOOK_AUTHOR.NAME).distinct(),
			)
		}

	fun toCategoryTree(
		record: Collection<BookCategoryRecord>,
		headId: Long,
	): BookCategoryTree {
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

		return generateCategoryWithDetails(
			categoryMap = categoryMap,
			recordMap = recordMap,
			headId = headId,
		)
	}

	private fun generateCategoryWithDetails(
		categoryMap: Map<Long, Set<Long>>,
		recordMap: Map<Long, BookCategoryRecord>,
		headId: Long,
	): BookCategoryTree {
		val targetRecord: BookCategoryRecord = recordMap[headId]!!

		return BookCategoryTree(
			id = targetRecord.id,
			name = targetRecord.koreanName,
			details =
				categoryMap[headId]!!
					.map {
						generateCategoryWithDetails(
							categoryMap = categoryMap,
							recordMap = recordMap,
							headId = it,
						)
					},
		)
	}

	fun toBookDetail(
		bookRecord: Map<Long, Result<out Record>>,
		categoriesRecord: Result<BookCategoryRecord>,
	): BookDetail? =
		bookRecord
			.map {
				BookDetail(
					id = it.key,
					isbn = it.value.getValues(BOOK.ISBN).first(),
					title = it.value.getValues(BOOK.TITLE).first(),
					publisher = it.value.getValues(BOOK.PUBLISHER).first(),
					description =
						it.value
							.getValues(BOOK.DESCRIPTION)
							.firstOrNull(),
					imageUrl = it.value.getValues(BOOK.IMAGE_URL).firstOrNull(),
					categories = toBookCategorySingles(categoriesRecord = categoriesRecord),
					authors = it.value.getValues(BOOK_AUTHOR.NAME).distinct(),
				)
			}.firstOrNull()

	private fun toBookCategorySingles(categoriesRecord: Result<BookCategoryRecord>): Collection<BookCategorySingle> {
		val visited: MutableSet<Long> = mutableSetOf()
		val result = mutableListOf<BookCategorySingle>()

		categoriesRecord.forEach {
			if (!visited.contains(it.id)) {
				toBookCategorySingleParents(
					categoriesRecord,
					it.id,
					visited,
				)?.let { c -> result.add(c) }
			}
		}

		return result
	}

	private fun toBookCategorySingleParents(
		categoriesRecord: Result<BookCategoryRecord>,
		id: Long,
		visited: MutableSet<Long>,
	): BookCategorySingle? {
		if (id == BookCategory.ROOT_ID) {
			return null
		}

		visited.add(id)
		val target = categoriesRecord.first { it.id == id }

		return BookCategorySingle(
			target.id,
			target.koreanName,
			toBookCategorySingleParents(
				categoriesRecord = categoriesRecord,
				id = target.parentId,
				visited = visited,
			),
		)
	}
}