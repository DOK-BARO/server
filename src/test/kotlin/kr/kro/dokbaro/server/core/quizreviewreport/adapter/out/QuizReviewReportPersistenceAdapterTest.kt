package kr.kro.dokbaro.server.core.quizreviewreport.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreviewreport.adapter.out.persistence.repository.jooq.QuizReviewReportRepository
import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class QuizReviewReportPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())

		val quizReviewRepository = QuizReviewRepository(dslContext)

		val quizReviewReportRepository = QuizReviewReportRepository(dslContext)

		val adapter = QuizReviewReportPersistenceAdapter(quizReviewReportRepository)

		"quizReviewReport를 저장한다" {
			val memberId: Long = memberRepository.insert(memberFixture()).id
			val bookId: Long = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val quizReviewId: Long =
				quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))

			adapter.insert(
				QuizReviewReport(
					quizReviewId = quizReviewId,
					reporterId = memberId,
					content = "hello",
				),
			) shouldNotBe null
		}
	})