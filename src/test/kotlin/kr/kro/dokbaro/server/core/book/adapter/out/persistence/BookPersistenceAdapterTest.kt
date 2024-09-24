package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.fixture.domain.bookCategoryFixture
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import org.jooq.DSLContext

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
					pagingOption = PagingOption(0, expectedCount.toLong()),
				).size shouldBe expectedCount
		}

		val defaultPagingOption = PagingOption(0, 200_000_000)

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

			queryAdapter.getAllBook(mobileCondition, defaultPagingOption).count() shouldBe
				books.count { it.categories.contains(mobileId) }

			queryAdapter.getAllBook(osCondition, defaultPagingOption).count() shouldBe
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

			queryAdapter.getAllBook(condition, defaultPagingOption).count() shouldBe
				books.count { it.authors.any { author -> author.name == target } }
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

			queryAdapter.getAllBook(condition, defaultPagingOption).count() shouldBe
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

			queryAdapter.getAllBook(condition, defaultPagingOption).count() shouldBe
				books.count { it.description?.contains(target) == true }
		}
	})