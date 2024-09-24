package kr.kro.dokbaro.server.core.book.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.fixture.domain.bookCategoryFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class BookCategoryPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		val bookRepository = BookRepository(dslContext)
		val adapter = BookCategoryPersistenceAdapter(bookRepository)

		"category를 생성한다" {
			val rootId = bookRepository.insertBookCategory(bookCategoryFixture())

			adapter.insert(
				BookCategory(
					koreanName = "모바일",
					englishName = "MOBILE",
					parentId = rootId,
				),
			) shouldNotBe null
		}
	})