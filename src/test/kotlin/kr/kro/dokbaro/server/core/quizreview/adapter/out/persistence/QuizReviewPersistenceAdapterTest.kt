package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq.QuizReviewMapper
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class QuizReviewPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())

		val quizReviewRepository = QuizReviewRepository(dslContext)
		val quizReviewQueryRepository = QuizReviewQueryRepository(dslContext, QuizReviewMapper())
		val adapter = QuizReviewPersistenceAdapter(quizReviewRepository, quizReviewQueryRepository)

		"생성을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val bookQuizId2: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			adapter.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId)) shouldNotBe null
			adapter.insert(quizReviewFixture(comment = "hello", memberId = memberId, quizId = bookQuizId2)) shouldNotBe null
		}

		"탐색을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val quizReviewId: Long = adapter.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))

			adapter.findBy(quizReviewId) shouldNotBe null
			adapter.findBy(quizReviewId + 1) shouldBe null
		}

		"수정을 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val quizReviewId: Long = adapter.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))

			val quizReview: QuizReview = quizReviewQueryRepository.findById(quizReviewId)!!

			val newComment = "newHello"
			quizReview.changeReview(2, 3, newComment)

			adapter.update(quizReview)

			val updatedQuizReview: QuizReview = quizReviewQueryRepository.findById(quizReviewId)!!

			updatedQuizReview.comment shouldBe newComment

			quizReview.changeReview(2, 3)

			adapter.update(quizReview)

			val updatedQuizReview2: QuizReview = quizReviewQueryRepository.findById(quizReviewId)!!

			updatedQuizReview2.comment shouldBe null
		}

		"삭제를 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val quizReviewId: Long = adapter.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))

			adapter.deleteBy(quizReviewId)

			quizReviewQueryRepository.findById(quizReviewId) shouldBe null
		}
	})