package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldBeNull
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
import org.jooq.generated.tables.pojos.BookCategory
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

		val pagingOption = BookCollectionPagingOption(0, 100)

		afterEach {
			dslContext.rollback()
		}

		"목록 조회를 수행한다" {
			bookDao.insert(BookFixture.entries.map { it.toJooqBook() })
			bookAuthorDao.insert(BookAuthorFixture.entries.map { it.toJooqBookAuthor() })
			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(BookCategoryGroupFixture.entries.map { it.toJooqBookCategoryGroup() })

			val result: Collection<Book> =
				adapter.getAllBook(
					LoadBookCollectionCondition(
						title = null,
						authorName = null,
						description = null,
						categoryId = null,
					),
					pagingOption,
				)

			result.size shouldBe BookFixture.entries.size
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

			adapter.getAllBook(LoadBookCollectionCondition(title = "이펙티브"), pagingOption).count() shouldBe 3
			adapter.getAllBook(LoadBookCollectionCondition(title = "베리"), pagingOption).count() shouldBe 2
			adapter.getAllBook(LoadBookCollectionCondition(title = "나다"), pagingOption).count() shouldBe 1
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
			adapter.getAllBook(LoadBookCollectionCondition(authorName = "김우근"), pagingOption).count() shouldBe 3
			adapter.getAllBook(LoadBookCollectionCondition(authorName = "박현준"), pagingOption).count() shouldBe 1
			adapter.getAllBook(LoadBookCollectionCondition(authorName = "조영호"), pagingOption).count() shouldBe 1
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

			adapter.getAllBook(LoadBookCollectionCondition(description = "좋은"), pagingOption).count() shouldBe 3
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

			adapter.getAllBook(LoadBookCollectionCondition(categoryId = 2), pagingOption).count() shouldBe 3
		}

		"페이징을 수행한다" {
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
					org.jooq.generated.tables.pojos
						.Book(6, "isbn6", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(7, "isbn7", "이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(8, "isbn8", "이펙티자버", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(9, "isbn9", "베리이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(10, "isbn10", "베리이펙티브", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(11, "isbn11", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
					org.jooq.generated.tables.pojos
						.Book(12, "isbn12", "가나다라마", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a"),
					BookAuthor(2, 2, "a"),
					BookAuthor(3, 3, "a"),
					BookAuthor(4, 4, "a"),
					BookAuthor(5, 5, "a"),
					BookAuthor(6, 6, "a"),
					BookAuthor(7, 7, "a"),
					BookAuthor(8, 8, "a"),
					BookAuthor(9, 9, "a"),
					BookAuthor(10, 10, "a"),
					BookAuthor(11, 11, "a"),
					BookAuthor(12, 12, "a"),
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
					BookCategoryGroup(7, 6, 2),
					BookCategoryGroup(8, 7, 1),
					BookCategoryGroup(9, 8, 1),
					BookCategoryGroup(10, 9, 2),
					BookCategoryGroup(11, 10, 25),
					BookCategoryGroup(12, 11, 2),
					BookCategoryGroup(13, 12, 2),
					BookCategoryGroup(14, 12, 8),
				),
			)

			adapter.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(0, 1)).count() shouldBe 1
			adapter.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(0, 2)).count() shouldBe 2
			adapter.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(10, 10)).forEach { println(it) }
			adapter.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(10, 10)).count() shouldBe 2
			adapter.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(0, 100)).count() shouldBe 12
			adapter
				.getAllBook(LoadBookCollectionCondition(), BookCollectionPagingOption(3, 100))
				.map { it.id } shouldContainAll listOf(4L, 5L, 6L)
		}

		"카테고리 목록을 조회한다" {
			bookCategoryDao.insert(
				listOf(
					BookCategory(1, "", "ROOT", null),
					BookCategory(2, "", "IT", 1),
					BookCategory(21, "", "IT-1", 2),
					BookCategory(211, "", "IT-1-1", 21),
					BookCategory(212, "", "IT-1-2", 21),
					BookCategory(2121, "", "IT-1-2-1", 212),
					BookCategory(2122, "", "IT-1-2-2", 212),
					BookCategory(213, "", "IT-1-2", 21),
					BookCategory(22, "", "IT-2", 2),
					BookCategory(23, "", "IT-2", 2),
					BookCategory(24, "", "IT-3", 2),
				),
			)

			val result: kr.kro.dokbaro.server.core.book.domain.BookCategory = adapter.getBookCategory(2)

			result.details.size shouldBe 4
			result.details
				.find { it.id == 21L }!!
				.details.size shouldBe 3

			result.details
				.find { it.id == 22L }!!
				.details
				.shouldBeEmpty()

			result.details
				.find { it.id == 21L }!!
				.details
				.find { it.id == 212L }!!
				.details
				.size shouldBe 2
		}

		"category id가 1일 때 전체 목록을 가져온다" {
			bookCategoryDao.insert(
				listOf(
					BookCategory(1, "", "ROOT", null),
					BookCategory(2, "", "IT", 1),
					BookCategory(21, "", "IT-1", 2),
					BookCategory(211, "", "IT-1-1", 21),
					BookCategory(212, "", "IT-1-2", 21),
					BookCategory(2121, "", "IT-1-2-1", 212),
					BookCategory(2122, "", "IT-1-2-2", 212),
					BookCategory(213, "", "IT-1-2", 21),
					BookCategory(22, "", "IT-2", 2),
					BookCategory(23, "", "IT-2", 2),
					BookCategory(24, "", "IT-3", 2),
				),
			)

			val result: kr.kro.dokbaro.server.core.book.domain.BookCategory = adapter.getBookCategory(1)

			result.details.size shouldBe 1
			result.details
				.find { it.id == 2L }!!
				.details
				.find { it.id == 21L }!!
				.details.size shouldBe 3

			result.details
				.find { it.id == 2L }!!
				.details
				.find { it.id == 22L }!!
				.details
				.shouldBeEmpty()

			result.details
				.find { it.id == 2L }!!
				.details
				.find { it.id == 21L }!!
				.details
				.find { it.id == 212L }!!
				.details
				.size shouldBe 2
		}

		"ID를 통한 책 조회를 수행한다" {
			val targetId = 1L
			bookDao.insert(
				org.jooq.generated.tables.pojos
					.Book(targetId, "isbn", "이펙티브자바", "출판사", LocalDate.now(), 4000, "".toByteArray(), "image"),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, targetId, "aaa"),
					BookAuthor(2, targetId, "abb"),
				),
			)
			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, targetId, 1),
					BookCategoryGroup(2, targetId, 2),
				),
			)

			val target = adapter.getBook(targetId)!!

			adapter.getBook(99).shouldBeNull()
			target.id shouldBe targetId
			target.categories.size shouldBe 2
			target.authors.size shouldBe 2
		}
	})