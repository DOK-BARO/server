package kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookCategorySingle
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.records.BookCategoryRecord
import org.springframework.stereotype.Component

@Component
class BookMapper {
	companion object {
		private val BOOK = JBook.BOOK
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
	}

	fun toSummaryCollection(record: Map<Long, Result<out Record>>): Collection<BookSummary> =
		record.map {
			BookSummary(
				it.key,
				it.value.getValues(BOOK.TITLE).first(),
				it.value.getValues(BOOK.PUBLISHER).first(),
				it.value.getValues(BOOK.IMAGE_URL).firstOrNull(),
				it.value.getValues(BOOK_AUTHOR.NAME).distinct(),
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

		return generateCategoryWithDetails(categoryMap, recordMap, headId)
	}

	private fun generateCategoryWithDetails(
		categoryMap: Map<Long, Set<Long>>,
		recordMap: Map<Long, BookCategoryRecord>,
		headId: Long,
	): BookCategoryTree {
		val targetRecord: BookCategoryRecord = recordMap[headId]!!

		return BookCategoryTree(
			targetRecord.id,
			targetRecord.koreanName,
			categoryMap[headId]!!
				.map { generateCategoryWithDetails(categoryMap, recordMap, it) },
		)
	}

	fun toBookDetail(
		bookRecord: Map<Long, Result<out Record>>,
		categoriesRecord: Result<BookCategoryRecord>,
	): BookDetail? =
		bookRecord
			.map {
				BookDetail(
					it.key,
					it.value.getValues(BOOK.ISBN).first(),
					it.value.getValues(BOOK.TITLE).first(),
					it.value.getValues(BOOK.PUBLISHER).first(),
					it.value
						.getValues(BOOK.DESCRIPTION)
						.firstOrNull()
						?.toString(Charsets.UTF_8),
					it.value.getValues(BOOK.IMAGE_URL).firstOrNull(),
					toBookCategorySingles(categoriesRecord),
					it.value.getValues(BOOK_AUTHOR.NAME).distinct(),
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
				categoriesRecord,
				target.parentId,
				visited,
			),
		)
	}
}