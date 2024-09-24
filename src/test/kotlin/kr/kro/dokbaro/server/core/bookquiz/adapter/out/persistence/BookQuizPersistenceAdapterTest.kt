package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import org.jooq.DSLContext

@PersistenceAdapterTest
class BookQuizPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookQuizRepository = BookQuizRepository(dslContext)
		val adapter = BookQuizPersistenceAdapter(bookQuizRepository)
	})