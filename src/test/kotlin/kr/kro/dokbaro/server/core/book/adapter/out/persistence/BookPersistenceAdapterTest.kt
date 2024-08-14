package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.application.port.out.dto.BookCollectionPagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.LoadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.fixture.book.BookAuthorFixture
import kr.kro.dokbaro.server.fixture.book.BookCategoryFixture
import kr.kro.dokbaro.server.fixture.book.BookCategoryGroupFixture
import kr.kro.dokbaro.server.fixture.book.BookFixture
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.BookAuthorDao
import org.jooq.generated.tables.daos.BookCategoryDao
import org.jooq.generated.tables.daos.BookCategoryGroupDao
import org.jooq.generated.tables.daos.BookDao
import org.jooq.generated.tables.pojos.BookAuthor
import org.jooq.generated.tables.pojos.BookCategoryGroup
import java.time.LocalDate

@PersistenceAdapterTest
class BookPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookDao = BookDao(configuration)
		val bookAuthorDao = BookAuthorDao(configuration)
		val bookCategoryDao = BookCategoryDao(configuration)
		val bookCategoryGroupDao = BookCategoryGroupDao(configuration)

		val mapper = BookMapper()
		val queryRepository = BookQueryRepository(dslContext, mapper)
		val adapter = BookPersistenceAdapter(queryRepository)

		val pagingOption = BookCollectionPagingOption(null, 100)
		afterEach {
			dslContext.rollback()
		}

		"목록 조회를 수행한다" {
			bookDao.insert(BookFixture.entries.map { it.toJooqBook() })
			bookAuthorDao.insert(BookAuthorFixture.entries.map { it.toJooqBookAuthor() })
			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(BookCategoryGroupFixture.entries.map { it.toJooqBookCategoryGroup() })

			val result: Collection<Book> =
				adapter.getAll(
					LoadBookCollectionCondition(
						title = null,
						authorName = null,
						description = null,
						categoryId = null,
					),
					pagingOption,
				)

			result.forEach { println(it) }
		}

		"이름을 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(1, "isbn", "이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(2, "isbn2", "이펙티자버", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(3, "isbn3", "베리이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(4, "isbn4", "베리이펙티브", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(5, "isbn5", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a"),
					BookAuthor(2, 2, "a"),
					BookAuthor(3, 3, "a"),
					BookAuthor(4, 4, "a"),
					BookAuthor(5, 5, "a"),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1),
					BookCategoryGroup(2, 2, 1),
					BookCategoryGroup(3, 3, 1),
					BookCategoryGroup(4, 4, 1),
					BookCategoryGroup(5, 5, 1),
				),
			)

			adapter.getAll(LoadBookCollectionCondition(title = "이펙티브"), pagingOption).count() shouldBe 3
			adapter.getAll(LoadBookCollectionCondition(title = "베리"), pagingOption).count() shouldBe 2
			adapter.getAll(LoadBookCollectionCondition(title = "나다"), pagingOption).count() shouldBe 1
		}

		"저자를 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(1, "isbn", "이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(2, "isbn2", "이펙티자버", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(3, "isbn3", "베리이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(4, "isbn4", "베리이펙티브", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(5, "isbn5", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "조영호"),
					BookAuthor(2, 2, "김우근"),
					BookAuthor(3, 3, "박현준"),
					BookAuthor(4, 3, "김우근"),
					BookAuthor(5, 4, "김우근"),
					BookAuthor(6, 5, "가나다"),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1),
					BookCategoryGroup(2, 2, 1),
					BookCategoryGroup(3, 3, 1),
					BookCategoryGroup(4, 4, 1),
					BookCategoryGroup(5, 5, 1),
				),
			)
			adapter.getAll(LoadBookCollectionCondition(authorName = "김우근"), pagingOption).count() shouldBe 3
			adapter.getAll(LoadBookCollectionCondition(authorName = "박현준"), pagingOption).count() shouldBe 1
			adapter.getAll(LoadBookCollectionCondition(authorName = "조영호"), pagingOption).count() shouldBe 1
		}

		"책 설명을 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(1, "isbn", "이펙티브자바", "출판사", LocalDate.now(), 4000, "매우좋은책!".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(2, "isbn2", "이펙티자버", "출판사", LocalDate.now(), 4000, "좋은책".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(3, "isbn3", "베리이펙티브자바", "출판사", LocalDate.now(), 4000, "정말좋은".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(4, "isbn4", "베리이펙티브", "출판사", LocalDate.now(), 4000, "가나다".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(5, "isbn5", "가나다라마", "출판사", LocalDate.now(), 4000, "좋매은즈".toByteArray(), "image"),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a"),
					BookAuthor(2, 2, "a"),
					BookAuthor(3, 3, "a"),
					BookAuthor(4, 4, "a"),
					BookAuthor(5, 5, "a"),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1),
					BookCategoryGroup(2, 2, 1),
					BookCategoryGroup(3, 3, 1),
					BookCategoryGroup(4, 4, 1),
					BookCategoryGroup(5, 5, 1),
				),
			)

			adapter.getAll(LoadBookCollectionCondition(description = "좋은"), pagingOption).count() shouldBe 3
		}

		"카테고리를 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(1, "isbn", "이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(2, "isbn2", "이펙티자버", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(3, "isbn3", "베리이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(4, "isbn4", "베리이펙티브", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(5, "isbn5", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a"),
					BookAuthor(2, 2, "a"),
					BookAuthor(3, 3, "a"),
					BookAuthor(4, 4, "a"),
					BookAuthor(5, 5, "a"),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1),
					BookCategoryGroup(2, 2, 1),
					BookCategoryGroup(3, 2, 2),
					BookCategoryGroup(4, 3, 25),
					BookCategoryGroup(5, 4, 2),
					BookCategoryGroup(6, 5, 2),
				),
			)

			adapter.getAll(LoadBookCollectionCondition(categoryId = 2), pagingOption).count() shouldBe 3
		}

		"페이징을 수행한다"
	})