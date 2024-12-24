package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRepository
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class SolvingQuizPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookRepository = BookRepository(dslContext)
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())
		val bookQuizQueryRepository = BookQuizQueryRepository(dslContext, BookQuizMapper())

		val solvingQuizRepository = SolvingQuizRepository(dslContext)
		val solvingQuizQueryRepository = SolvingQuizQueryRepository(dslContext, SolvingQuizMapper())

		val adapter = SolvingQuizPersistenceAdapter(solvingQuizRepository)

		"생성을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			adapter.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId)) shouldNotBe null
		}

		"ID를 통한 탐색을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			val solvingQuizId = adapter.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))

			solvingQuizQueryRepository.findById(solvingQuizId) shouldNotBe null
		}

		"수정을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val bookQuiz = bookQuizQueryRepository.findBookQuizQuestionsBy(bookQuizId)!!

			val solvingQuizId = adapter.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))

			val solvingQuiz: SolvingQuiz = solvingQuizQueryRepository.findById(solvingQuizId)!!

			val targetQuestionId = bookQuiz.questions.map { it.id }.first()
			solvingQuiz.addSheet(targetQuestionId, AnswerSheet(listOf("hello")))

			adapter.update(solvingQuiz)

			solvingQuizQueryRepository.findById(solvingQuizId)!!.getSheets()[targetQuestionId] shouldNotBe null
		}
	})