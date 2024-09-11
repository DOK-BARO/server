package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.BookDao

@PersistenceAdapterTest
class BookQuizPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookQuizRepository = BookQuizRepository(dslContext)

		val bookDao = BookDao(configuration)
		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val adapter = BookQuizPersistenceAdapter(bookQuizRepository)

		adapter.insert(
			BookQuiz(
				title = "title",
				description = "description",
				bookId = 1,
				creatorMemberId = 2,
			),
		)
	})