package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookSummarySortOption
import kr.kro.dokbaro.server.fixture.domain.bookCategoryFixture
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import org.jooq.DSLContext
import java.time.LocalDate

@PersistenceAdapterTest
class BookPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val repository = BookRepository(dslContext)
		val queryRepository = BookQueryRepository(dslContext, BookMapper())
		val adapter = BookPersistenceAdapter(repository)

		val queryAdapter = BookPersistenceQueryAdapter(queryRepository)

		"신규 책 생성을 수행 후 조회한다" {
			val mobileId =
				repository.insertBookCategory(bookCategoryFixture(parentId = BookCategory.ROOT_ID, koreanName = "모바일"))
			val iosId = repository.insertBookCategory(bookCategoryFixture(parentId = mobileId, koreanName = "IOS"))
			val androidId = repository.insertBookCategory(bookCategoryFixture(parentId = mobileId, koreanName = "안드로이드"))
			val book = bookFixture(categories = setOf(iosId, androidId))
			val savedId = adapter.insert(book)

			savedId.shouldNotBeNull()

			val findBook = queryAdapter.findBy(savedId)!!

			findBook.id shouldBe savedId
			findBook.isbn shouldBe book.isbn
			findBook.title shouldBe book.title
			findBook.publisher shouldBe book.publisher
			findBook.description shouldBe book.description
			findBook.imageUrl shouldBe book.imageUrl
			findBook.categories.map { it.id } shouldContainAll book.categories
			findBook.authors shouldContainAll book.authors.map { it.name }
		}

		"신규 책 생성 시 책 설명이 null 이어도 생성을 진행한다" {
			val categoryId =
				repository.insertBookCategory(bookCategoryFixture(parentId = BookCategory.ROOT_ID, koreanName = "모바일"))
			val book = bookFixture(categories = setOf(categoryId), description = null)
			val savedId = adapter.insert(book)

			savedId.shouldNotBeNull()

			val findBook = queryAdapter.findBy(savedId)!!

			findBook.id shouldBe savedId
			findBook.description shouldBe null
		}

		val defaultPageOption = PageOption(0, 200_000_000)
		val defaultSortOption = SortOption(BookSummarySortOption.TITLE)
		"책 목록을 조회한다" {
			val categoryId =
				repository.insertBookCategory(bookCategoryFixture(parentId = BookCategory.ROOT_ID, koreanName = "모바일"))
			val expectedCount = 10
			(1..expectedCount)
				.map { bookFixture(categories = setOf(categoryId)) }
				.forEach { adapter.insert(it) }

			queryAdapter
				.getAllBook(
					condition = ReadBookCollectionCondition(),
					pageOption = PageOption(0, expectedCount.toLong()),
					sortOption = defaultSortOption,
				).size shouldBe expectedCount
		}

		"category를 통한 목록 조회를 수행한다" {
			val mobileId =
				repository.insertBookCategory(bookCategoryFixture(parentId = BookCategory.ROOT_ID, koreanName = "모바일"))
			val osId =
				repository.insertBookCategory(bookCategoryFixture(parentId = BookCategory.ROOT_ID, koreanName = "운영체제"))

			val books =
				listOf(
					bookFixture(categories = setOf(mobileId)),
					bookFixture(categories = setOf(osId)),
					bookFixture(categories = setOf(mobileId, osId)),
				)
			books.forEach {
				adapter.insert(it)
			}

			val mobileCondition = ReadBookCollectionCondition(categoryId = mobileId)
			val osCondition = ReadBookCollectionCondition(categoryId = osId)

			queryAdapter.getAllBook(mobileCondition, defaultPageOption, defaultSortOption).count() shouldBe
				books.count { it.categories.contains(mobileId) }

			queryAdapter.getAllBook(osCondition, defaultPageOption, defaultSortOption).count() shouldBe
				books.count { it.categories.contains(osId) }
		}

		"저자 명을 통한 목록 조회를 수행한다" {
			val books =
				listOf(
					bookFixture(authors = listOf("박현준", "기기")),
					bookFixture(authors = listOf("박현준")),
					bookFixture(authors = listOf("주주", "가가")),
					bookFixture(authors = listOf("현준", "가가")),
					bookFixture(authors = listOf("현")),
				)
			books.forEach {
				adapter.insert(it)
			}

			val target = "현"
			val condition = ReadBookCollectionCondition(authorName = target)

			queryAdapter.getAllBook(condition, defaultPageOption, defaultSortOption).count() shouldBe
				books.count { it.authors.any { author -> author.name.contains(target) } }
		}

		"제목이 포함된 책만 조회를 수행한다" {
			val books =
				listOf(
					bookFixture(title = "모두의자바"),
					bookFixture(title = "자바스크립트"),
					bookFixture(title = "자바의정석"),
					bookFixture(title = "가나자바"),
					bookFixture(title = "객사오"),
				)
			books.forEach {
				adapter.insert(it)
			}

			val target = "자바"
			val condition = ReadBookCollectionCondition(title = target)

			queryAdapter.getAllBook(condition, defaultPageOption, defaultSortOption).count() shouldBe
				books.count { it.title.contains(target) }
		}

		"설명이 포함된 책만 조회를 수행한다" {
			val books =
				listOf(
					bookFixture(description = "모두의자바"),
					bookFixture(description = "자바스크립트"),
					bookFixture(description = "자바의정석"),
					bookFixture(description = "가나자바"),
					bookFixture(description = "객사오"),
				)
			books.forEach {
				adapter.insert(it)
			}

			val target = "자바"
			val condition = ReadBookCollectionCondition(description = target)

			queryAdapter.getAllBook(condition, defaultPageOption, defaultSortOption).count() shouldBe
				books.count { it.description?.contains(target) == true }
		}

		"통합 검색을 수행한다" {
			val keyword = "자바"
			val books =
				listOf(
					bookFixture(title = "모두의$keyword"),
					bookFixture(title = "${keyword}스크립트"),
					bookFixture(title = "깍", authors = listOf("${keyword}의정석")),
					bookFixture(title = "액션${keyword}스크립트", authors = listOf("${keyword}의정석")),
					bookFixture(title = "한"),
				)
			books.forEach {
				adapter.insert(it)
			}

			queryAdapter.findAllIntegratedBook(defaultPageOption, keyword).size shouldBe 4
		}

		"조회한 결과를 정렬한다" {
			val books =
				listOf(
					bookFixture(title = "A", publishedAt = LocalDate.of(2024, 1, 5)),
					bookFixture(title = "B", publishedAt = LocalDate.of(2024, 1, 4)),
					bookFixture(title = "C", publishedAt = LocalDate.of(2024, 1, 3)),
					bookFixture(title = "D", publishedAt = LocalDate.of(2024, 1, 2)),
					bookFixture(title = "E", publishedAt = LocalDate.of(2024, 1, 1)),
				)
			books.forEach {
				adapter.insert(it)
			}
			val condition = ReadBookCollectionCondition()

			queryAdapter
				.getAllBook(
					condition,
					defaultPageOption,
					SortOption(BookSummarySortOption.TITLE, SortDirection.ASC),
				).toList()[0]
				.title shouldBe "A"

			val title =
				queryAdapter
					.getAllBook(
						condition,
						defaultPageOption,
						SortOption(BookSummarySortOption.PUBLISHED_AT, SortDirection.ASC),
					).toList()[0]
					.title
			title shouldBe "A"

			queryAdapter
				.getAllBook(
					condition,
					defaultPageOption,
					SortOption(BookSummarySortOption.QUIZ_COUNT, SortDirection.DESC),
				).toList()[0] shouldNotBe null
		}

		"개수를 조회한다" {
			val books =
				listOf(
					bookFixture(title = "A", publishedAt = LocalDate.of(2024, 1, 5)),
					bookFixture(title = "AA", publishedAt = LocalDate.of(2024, 1, 4)),
					bookFixture(title = "AAA", publishedAt = LocalDate.of(2024, 1, 3)),
					bookFixture(title = "D", publishedAt = LocalDate.of(2024, 1, 2)),
					bookFixture(title = "E", publishedAt = LocalDate.of(2024, 1, 1)),
				)
			books.forEach {
				adapter.insert(it)
			}

			queryAdapter.countBy(ReadBookCollectionCondition()) shouldBe 5
			queryAdapter.countBy(ReadBookCollectionCondition(title = "A")) shouldBe 3
		}
	})