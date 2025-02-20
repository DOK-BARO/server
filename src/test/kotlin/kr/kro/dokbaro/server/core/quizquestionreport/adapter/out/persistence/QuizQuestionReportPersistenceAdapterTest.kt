package kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence

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
import kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence.repository.jooq.QuizQuestionReportRepository
import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class QuizQuestionReportPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())

		val adapter =
			QuizQuestionReportPersistenceAdapter(
				QuizQuestionReportRepository(dslContext),
			)

		"quizQuestionReport 를 저장한다" {
			val memberId: Long = memberRepository.insert(memberFixture()).id
			val bookId: Long = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			val bookQuiz = bookQuizRepository.load(bookQuizId)!!

			val quizQuestionReport =
				QuizQuestionReport(
					questionId =
						bookQuiz.questions
							.getQuestions()
							.first()
							.id,
					reporterId = memberId,
					contents = listOf("hello"),
				)

			adapter.insert(quizQuestionReport) shouldNotBe null
		}
	})