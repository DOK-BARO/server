package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.entity.jooq.BookMapper
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookQueryRepository
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.fixture.domain.bookCategoryFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class BookCategoryPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val bookQueryRepository = BookQueryRepository(dslContext, BookMapper())

		val adapter = BookCategoryPersistenceQueryAdapter(bookQueryRepository)

		"category tree를 조회한다" {
			val rootId = bookRepository.insertBookCategory(bookCategoryFixture())
			val osId = bookRepository.insertBookCategory(bookCategoryFixture(parentId = rootId, koreanName = "OS"))
			val linuxId = bookRepository.insertBookCategory(bookCategoryFixture(parentId = osId, koreanName = "LINUX"))
			val windowId = bookRepository.insertBookCategory(bookCategoryFixture(parentId = osId, koreanName = "MAC"))
			val macId = bookRepository.insertBookCategory(bookCategoryFixture(parentId = osId, koreanName = "WINDOWS"))

			adapter.findTreeBy(rootId).id shouldBe rootId
			adapter.findTreeBy(osId).id shouldBe osId
			adapter.findTreeBy(linuxId).id shouldBe linuxId
			adapter.findTreeBy(windowId).id shouldBe windowId
			adapter.findTreeBy(macId).id shouldBe macId

			adapter.findTreeBy(rootId).details.count() shouldBe 1
			adapter.findTreeBy(osId).details.count() shouldBe 3
			adapter.findTreeBy(macId).details.count() shouldBe 0
		}
	})