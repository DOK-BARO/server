package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
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
import java.time.LocalDateTime

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
		val queryRepository = BookRepository(dslContext, mapper)
		val adapter = BookPersistenceAdapter(queryRepository)

		val pagingOption = PagingOption(0, 100)

		"목록 조회를 수행한다" {
			bookDao.insert(BookFixture.entries.map { it.toJooqBook() })
			bookAuthorDao.insert(BookAuthorFixture.entries.map { it.toJooqBookAuthor() })
			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(BookCategoryGroupFixture.entries.map { it.toJooqBookCategoryGroup() })

			val result: Collection<Book> =
				adapter.getAllBook(
					ReadBookCollectionCondition(
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
						.Book(
							1,
							"isbn",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							2,
							"isbn2",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							3,
							"isbn3",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							4,
							"isbn4",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							5,
							"isbn5",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, 2, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(3, 3, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(4, 4, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(5, 5, "a", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, 2, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(3, 3, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(4, 4, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(5, 5, 1, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			adapter.getAllBook(ReadBookCollectionCondition(title = "이펙티브"), pagingOption).count() shouldBe 3
			adapter.getAllBook(ReadBookCollectionCondition(title = "베리"), pagingOption).count() shouldBe 2
			adapter.getAllBook(ReadBookCollectionCondition(title = "나다"), pagingOption).count() shouldBe 1
		}

		"저자를 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(
							1,
							"isbn",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							2,
							"isbn2",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							3,
							"isbn3",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							4,
							"isbn4",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							5,
							"isbn5",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "조영호", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, 2, "김우근", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(3, 3, "박현준", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(4, 3, "김우근", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(5, 4, "김우근", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(6, 5, "가나다", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, 2, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(3, 3, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(4, 4, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(5, 5, 1, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)
			adapter.getAllBook(ReadBookCollectionCondition(authorName = "김우근"), pagingOption).count() shouldBe 3
			adapter.getAllBook(ReadBookCollectionCondition(authorName = "박현준"), pagingOption).count() shouldBe 1
			adapter.getAllBook(ReadBookCollectionCondition(authorName = "조영호"), pagingOption).count() shouldBe 1
		}

		"책 설명을 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(
							1,
							"isbn",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"매우좋은책!".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							2,
							"isbn2",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"좋은책".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							3,
							"isbn3",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"정말좋은".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							4,
							"isbn4",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"가나다".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							5,
							"isbn5",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"좋매은즈".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, 2, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(3, 3, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(4, 4, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(5, 5, "a", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, 2, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(3, 3, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(4, 4, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(5, 5, 1, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			adapter.getAllBook(ReadBookCollectionCondition(description = "좋은"), pagingOption).count() shouldBe 3
		}

		"카테고리를 통한 검색을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(
							1,
							"isbn",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							2,
							"isbn2",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							3,
							"isbn3",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							4,
							"isbn4",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							5,
							"isbn5",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, 2, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(3, 3, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(4, 4, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(5, 5, "a", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, 2, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(3, 2, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(4, 3, 25, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(5, 4, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(6, 5, 2, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			adapter.getAllBook(ReadBookCollectionCondition(categoryId = 2), pagingOption).count() shouldBe 3
		}

		"페이징을 수행한다" {
			bookDao.insert(
				listOf(
					org.jooq.generated.tables.pojos
						.Book(
							1,
							"isbn",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							2,
							"isbn2",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							3,
							"isbn3",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							4,
							"isbn4",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							5,
							"isbn5",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							6,
							"isbn6",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							7,
							"isbn7",
							"이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							8,
							"isbn8",
							"이펙티자버",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							9,
							"isbn9",
							"베리이펙티브자바",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							10,
							"isbn10",
							"베리이펙티브",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							11,
							"isbn11",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
					org.jooq.generated.tables.pojos
						.Book(
							12,
							"isbn12",
							"가나다라마",
							"출판사",
							LocalDate.now(),
							4000,
							"".toByteArray(),
							"image",
							LocalDateTime.now(),
							LocalDateTime.now(),
							false,
						),
				),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, 1, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, 2, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(3, 3, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(4, 4, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(5, 5, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(6, 6, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(7, 7, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(8, 8, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(9, 9, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(10, 10, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(11, 11, "a", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(12, 12, "a", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, 1, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, 2, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(3, 2, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(4, 3, 25, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(5, 4, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(6, 5, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(7, 6, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(8, 7, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(9, 8, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(10, 9, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(11, 10, 25, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(12, 11, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(13, 12, 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(14, 12, 8, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			adapter.getAllBook(ReadBookCollectionCondition(), PagingOption(0, 1)).count() shouldBe 1
			adapter.getAllBook(ReadBookCollectionCondition(), PagingOption(0, 2)).count() shouldBe 2
			adapter.getAllBook(ReadBookCollectionCondition(), PagingOption(10, 10)).forEach { println(it) }
			adapter.getAllBook(ReadBookCollectionCondition(), PagingOption(10, 10)).count() shouldBe 2
			adapter.getAllBook(ReadBookCollectionCondition(), PagingOption(0, 100)).count() shouldBe 12
			adapter
				.getAllBook(ReadBookCollectionCondition(), PagingOption(3, 100))
				.map { it.id } shouldContainAll listOf(4L, 5L, 6L)
		}

		"카테고리 목록을 조회한다" {
			bookCategoryDao.insert(
				listOf(
					BookCategory(1, "", "ROOT", null, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2, "", "IT", 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(21, "", "IT-1", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(211, "", "IT-1-1", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(212, "", "IT-1-2", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2121, "", "IT-1-2-1", 212, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2122, "", "IT-1-2-2", 212, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(213, "", "IT-1-2", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(22, "", "IT-2", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(23, "", "IT-2", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(24, "", "IT-3", 2, LocalDateTime.now(), LocalDateTime.now(), false),
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
					BookCategory(1, "", "ROOT", null, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2, "", "IT", 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(21, "", "IT-1", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(211, "", "IT-1-1", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(212, "", "IT-1-2", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2121, "", "IT-1-2-1", 212, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(2122, "", "IT-1-2-2", 212, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(213, "", "IT-1-2", 21, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(22, "", "IT-2", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(23, "", "IT-2", 2, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategory(24, "", "IT-3", 2, LocalDateTime.now(), LocalDateTime.now(), false),
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
					.Book(
						targetId,
						"isbn",
						"이펙티브자바",
						"출판사",
						LocalDate.now(),
						4000,
						"".toByteArray(),
						"image",
						LocalDateTime.now(),
						LocalDateTime.now(),
						false,
					),
			)
			bookAuthorDao.insert(
				listOf(
					BookAuthor(1, targetId, "aaa", LocalDateTime.now(), LocalDateTime.now(), false),
					BookAuthor(2, targetId, "abb", LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)
			bookCategoryDao.insert(BookCategoryFixture.entries.map { it.toJooqBookCategory() })
			bookCategoryGroupDao.insert(
				listOf(
					BookCategoryGroup(1, targetId, 1, LocalDateTime.now(), LocalDateTime.now(), false),
					BookCategoryGroup(2, targetId, 2, LocalDateTime.now(), LocalDateTime.now(), false),
				),
			)

			val target = adapter.getBook(targetId)!!

			adapter.getBook(99).shouldBeNull()
			target.id shouldBe targetId
			target.categories.size shouldBe 2
			target.authors.size shouldBe 2
		}
	})