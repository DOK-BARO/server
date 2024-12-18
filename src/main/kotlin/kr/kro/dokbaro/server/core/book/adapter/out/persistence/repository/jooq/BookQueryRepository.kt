package kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.BookSummarySortKeyword
import kr.kro.dokbaro.server.core.book.query.IntegratedBook
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.OrderField
import org.jooq.Record
import org.jooq.Result
import org.jooq.Table
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookAuthor
import org.jooq.generated.tables.JBookCategory
import org.jooq.generated.tables.JBookCategoryGroup
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.records.BookCategoryRecord
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.select
import org.jooq.impl.DSL.selectCount
import org.jooq.impl.DSL.table
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
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
	}

	fun findAllBookBy(
		condition: ReadBookCollectionCondition,
		pageOption: PageOption<BookSummarySortKeyword>,
	): Collection<BookSummary> {
		val bookTable = "book"

		val books: Table<out Record> =
			select(
				BOOK.ID,
				BOOK.TITLE,
				BOOK.PUBLISHER,
				BOOK.IMAGE_URL,
				field(
					selectCount()
						.from(BOOK_QUIZ)
						.where(BOOK_QUIZ.BOOK_ID.eq(BOOK.ID)),
				).`as`(BookRecordFieldName.QUIZ_COUNT.name),
			).from(BOOK)
				.where(buildCondition(condition))
				.orderBy(toOrderQuery(pageOption), BOOK.ID)
				.limit(pageOption.limit)
				.offset(pageOption.offset)
				.asTable(bookTable)

		val record: Map<Long, Result<out Record>> = getSummaryRecord(books)

		return bookMapper.toSummaryCollection(record)
	}

	private fun toOrderQuery(pageOption: PageOption<BookSummarySortKeyword>): OrderField<out Any> {
		val query =
			when (pageOption.sort) {
				BookSummarySortKeyword.PUBLISHED_AT -> BOOK.PUBLISHED_AT
				BookSummarySortKeyword.TITLE -> BOOK.TITLE
				BookSummarySortKeyword.QUIZ_COUNT -> field(BookRecordFieldName.QUIZ_COUNT.name)
			}

		if (pageOption.direction == SortDirection.DESC) {
			return query.desc()
		}

		return query
	}

	private fun buildCondition(condition: ReadBookCollectionCondition): Condition =
		DSL.and(
			condition.title?.let {
				BOOK.TITLE.likeIgnoreCase("%$it%")
			},
			condition.authorName?.let {
				BOOK.ID.`in`(
					select(BOOK_AUTHOR.BOOK_ID)
						.from(BOOK_AUTHOR)
						.where(BOOK_AUTHOR.BOOK_ID.eq(BOOK.ID))
						.and(BOOK_AUTHOR.NAME.likeIgnoreCase("%$it%")),
				)
			},
			condition.description?.let {
				BOOK.DESCRIPTION.likeIgnoreCase("%$it%")
			},
			condition.categoryId?.let {
				val hierarchyTable = "CategoryHierarchy"
				val hierarchyTableAlias = "ch"

				BOOK.ID.`in`(
					dslContext
						.select(BOOK_CATEGORY_GROUP.BOOK_ID)
						.from(BOOK_CATEGORY_GROUP)
						.where(
							BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID.`in`(
								dslContext
									.withRecursive(hierarchyTable)
									.`as`(
										dslContext
											.select(
												BOOK_CATEGORY.ID,
											).from(BOOK_CATEGORY)
											.where(BOOK_CATEGORY.ID.eq(it))
											.unionAll(
												dslContext
													.select(
														BOOK_CATEGORY.ID,
													).from(BOOK_CATEGORY)
													.join(table(name(hierarchyTable)).`as`(hierarchyTableAlias))
													.on(
														field(
															name(hierarchyTableAlias, "id"),
															Long::class.java,
														).eq(BOOK_CATEGORY.PARENT_ID),
													),
											),
									).select()
									.from(name(hierarchyTable))
									.fetchInto(Long::class.java),
							),
						),
				)
			},
		)

	fun findAllCategoryBy(id: Long): BookCategoryTree {
		val hierarchyTable = "CategoryHierarchy"
		val hierarchyTableAlias = "ch"

		val result: Collection<BookCategoryRecord> =
			dslContext
				.withRecursive(hierarchyTable)
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
								.join(table(name(hierarchyTable)).`as`(hierarchyTableAlias))
								.on(
									field(
										name(hierarchyTableAlias, "id"),
										Long::class.java,
									).eq(BOOK_CATEGORY.PARENT_ID),
								),
						),
				).select()
				.from(name(hierarchyTable))
				.fetchInto(BOOK_CATEGORY)

		return bookMapper.toCategoryTree(record = result, headId = id)
	}

	fun findById(id: Long): BookDetail? {
		val bookRecord: Map<Long, Result<out Record>> =
			dslContext
				.select(
					BOOK.ID,
					BOOK.ISBN,
					BOOK.TITLE,
					BOOK.PUBLISHER,
					BOOK.DESCRIPTION,
					BOOK.IMAGE_URL,
					BOOK_AUTHOR.NAME,
				).from(BOOK)
				.join(BOOK_AUTHOR)
				.on(BOOK.ID.eq(BOOK_AUTHOR.BOOK_ID))
				.join(BOOK_CATEGORY_GROUP)
				.on(BOOK.ID.eq(BOOK_CATEGORY_GROUP.BOOK_ID))
				.join(BOOK_CATEGORY)
				.on(BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID.eq(BOOK_CATEGORY.ID))
				.where(BOOK.ID.eq(id))
				.fetchGroups(BOOK.ID)

		val categoriesRecord: Result<BookCategoryRecord> = findCategoriesOfBook(bookId = id)

		return bookMapper.toBookDetail(bookRecord = bookRecord, categoriesRecord = categoriesRecord)
	}

	private fun findCategoriesOfBook(bookId: Long): Result<BookCategoryRecord> {
		val hierarchyTable = "CategoryHierarchy"
		val hierarchyTableAlias = "ch"

		return dslContext
			.withRecursive(hierarchyTable)
			.`as`(
				dslContext
					.select(
						BOOK_CATEGORY.ID,
						BOOK_CATEGORY.KOREAN_NAME,
						BOOK_CATEGORY.PARENT_ID,
					).from(BOOK_CATEGORY)
					.join(BOOK_CATEGORY_GROUP)
					.on(BOOK_CATEGORY_GROUP.BOOK_CATEGORY_ID.eq(BOOK_CATEGORY.ID))
					.where(BOOK_CATEGORY_GROUP.BOOK_ID.eq(bookId))
					.unionAll(
						dslContext
							.select(
								BOOK_CATEGORY.ID,
								BOOK_CATEGORY.KOREAN_NAME,
								BOOK_CATEGORY.PARENT_ID,
							).from(BOOK_CATEGORY)
							.join(table(name(hierarchyTable)).`as`(hierarchyTableAlias))
							.on(
								field(
									name(hierarchyTableAlias, "parent_id"),
									Long::class.java,
								).eq(BOOK_CATEGORY.ID),
							),
					),
			).select()
			.from(name(hierarchyTable))
			.fetchInto(BOOK_CATEGORY)
	}

	fun findAllIntegratedBook(
		size: Long,
		keyword: String,
		lastId: Long?,
	): Collection<IntegratedBook> {
		val bookTable = "book"

		val books: Table<out Record> =
			select(
				BOOK.ID,
				BOOK.TITLE,
				BOOK.PUBLISHER,
				BOOK.IMAGE_URL,
			).from(BOOK)
				.where(
					BOOK.ID
						.`in`(
							select(BOOK_AUTHOR.BOOK_ID)
								.from(BOOK_AUTHOR)
								.where(BOOK_AUTHOR.BOOK_ID.eq(BOOK.ID))
								.and(BOOK_AUTHOR.NAME.likeIgnoreCase("%$keyword%")),
						).or((BOOK.TITLE.likeIgnoreCase("%$keyword%"))),
				).orderBy(BOOK.ID)
				.seek(lastId ?: Constants.UNSAVED_ID)
				.limit(size)
				.asTable(bookTable)

		val record: Map<Long, Result<out Record>> = getSummaryRecord(books)

		return bookMapper.toIntegratedBookCollection(record)
	}

	private fun getSummaryRecord(books: Table<out Record>): Map<Long, Result<out Record>> =
		dslContext
			.select(
				BOOK.ID,
				BOOK.TITLE,
				BOOK.PUBLISHER,
				BOOK.IMAGE_URL,
				BOOK_AUTHOR.NAME,
				books.field(BookRecordFieldName.QUIZ_COUNT.name, Long::class.java),
			).from(books)
			.join(BOOK_AUTHOR)
			.on(books.field(BOOK.ID)!!.eq(BOOK_AUTHOR.BOOK_ID))
			.join(BOOK_CATEGORY_GROUP)
			.on(books.field(BOOK.ID)!!.eq(BOOK_CATEGORY_GROUP.BOOK_ID))
			.fetchGroups(BOOK.ID)

	fun countBy(condition: ReadBookCollectionCondition): Long =
		dslContext
			.selectCount()
			.from(BOOK)
			.where(buildCondition(condition))
			.fetchOneInto(Long::class.java)!!
}