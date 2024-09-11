package kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.Table
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategory
import org.jooq.generated.tables.JBookCategoryGroup
import org.jooq.generated.tables.records.BookCategoryRecord
import org.jooq.generated.tables.records.BookRecord
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.table
import org.jooq.impl.DSL.`val`
import org.springframework.stereotype.Repository

@Repository
class BookQueryRepository(
	private val dslContext: DSLContext,
	private val bookMapper: BookMapper,
) {
	companion object {
		private val BOOK = JBook.BOOK
		private val BOOK_AUTHOR = JBookAuthor.BOOK_AUTHOR
		private val BOOK_CATEGORY = JBookCategory.BOOK_CATEGORY
		private val BOOK_CATEGORY_GROUP = JBookCategoryGroup.BOOK_CATEGORY_GROUP
	}

	fun findAllBookBy(
		condition: ReadBookCollectionCondition,
		pagingOption: PagingOption,
	): Collection<Book> {
		val bookTable = "book"

		val books: Table<Record> =
			select()
				.from(BOOK)
				.where(buildCondition(condition))
				.orderBy(BOOK.ID)
				.limit(pagingOption.limit)
				.offset(pagingOption.offset)
				.asTable(bookTable)

		val record: Map<BookRecord, Result<Record>> =
			dslContext
				.select()
				.from(books)
				.join(BOOK_AUTHOR)
				.on(books.field(BOOK.ID)!!.eq(BOOK_AUTHOR.BOOK_ID))
				.join(BOOK_CATEGORY_GROUP)
				.on(books.field(BOOK.ID)!!.eq(BOOK_CATEGORY_GROUP.BOOK_ID))
				.join(BOOK_CATEGORY)
				.on(BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID.eq(BOOK_CATEGORY.ID))
				.fetchGroups(BOOK)

		return bookMapper.mapToBookCollection(record)
	}

	private fun buildCondition(condition: ReadBookCollectionCondition): Condition {
		var result: Condition = DSL.noCondition()

		condition.title?.let {
			result = result.and(BOOK.TITLE.likeIgnoreCase("%$it%"))
		}

		condition.authorName?.let {
			result =
				result.and(
					`val`(it).`in`(
						select(BOOK_AUTHOR.NAME).from(BOOK_AUTHOR).where(
							BOOK_AUTHOR.BOOK_ID.eq(BOOK.ID),
						),
					),
				)
		}

		condition.description?.let {
			result = result.and(BOOK.DESCRIPTION.likeIgnoreCase("%$it%"))
		}

		condition.categoryId?.let {
			result =
				result.and(
					`val`(it).`in`(
						select(BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID)
							.from(BOOK_CATEGORY_GROUP)
							.where(
								BOOK_CATEGORY_GROUP.BOOK_ID.eq(BOOK.ID),
							),
					),
				)
		}
		return result
	}

	fun findAllCategoryBy(id: Long): BookCategory {
		val hierarchyTableName = "CategoryHierarchy"
		val hierarchyTableAlias = "ch"

		val result: Collection<BookCategoryRecord> =
			dslContext
				.withRecursive(hierarchyTableName)
				.`as`(
					dslContext
						.select(
							BOOK_CATEGORY.ID,
							BOOK_CATEGORY.ENGLISH_NAME,
							BOOK_CATEGORY.KOREAN_NAME,
							BOOK_CATEGORY.PARENT_ID,
						).from(BOOK_CATEGORY)
						.where(BOOK_CATEGORY.ID.eq(id))
						.unionAll(
							dslContext
								.select(
									BOOK_CATEGORY.ID,
									BOOK_CATEGORY.ENGLISH_NAME,
									BOOK_CATEGORY.KOREAN_NAME,
									BOOK_CATEGORY.PARENT_ID,
								).from(BOOK_CATEGORY)
								.join(table(name(hierarchyTableName)).`as`(hierarchyTableAlias))
								.on(
									field(
										name(hierarchyTableAlias, "id"),
										Long::class.java,
									).eq(BOOK_CATEGORY.PARENT_ID),
								),
						),
				).select()
				.from(name(hierarchyTableName))
				.fetchInto(BOOK_CATEGORY)

		return bookMapper.mapToCategory(result, id)
	}

	fun findById(id: Long): Book? {
		val record: Map<BookRecord, Result<Record>> =
			dslContext
				.select()
				.from(BOOK)
				.join(BOOK_AUTHOR)
				.on(BOOK.ID.eq(BOOK_AUTHOR.BOOK_ID))
				.join(BOOK_CATEGORY_GROUP)
				.on(BOOK.ID.eq(BOOK_CATEGORY_GROUP.BOOK_ID))
				.join(BOOK_CATEGORY)
				.on(BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID.eq(BOOK_CATEGORY.ID))
				.where(BOOK.ID.eq(id))
				.fetchGroups(BOOK)

		return bookMapper.mapToBook(record)
	}
}