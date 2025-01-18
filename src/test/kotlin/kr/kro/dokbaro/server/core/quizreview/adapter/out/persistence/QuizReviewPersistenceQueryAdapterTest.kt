package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq.QuizReviewMapper
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class QuizReviewPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())
		val quizReviewRepository = QuizReviewRepository(dslContext)

		val quizReviewQueryRepository = QuizReviewQueryRepository(dslContext, QuizReviewMapper())
		val adapter = QuizReviewPersistenceQueryAdapter(quizReviewQueryRepository)

		"퀴즈 리뷰 평점 및 난이도 조회를 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val memberId2 = memberRepository.insert(memberFixture(email = Email("member2@mmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val bookQuizId2: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(memberId = memberId2, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(comment = "hello", memberId = memberId, quizId = bookQuizId2))

			adapter.findBy(bookQuizId).size shouldBe 2
		}

		"개수를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val memberId2 = memberRepository.insert(memberFixture(email = Email("member2@mmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))
			val bookQuizId2: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(memberId = memberId2, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(comment = "hello", memberId = memberId, quizId = bookQuizId2))

			adapter.countBy(CountQuizReviewCondition(bookQuizId)) shouldBe 2
		}

		"요약본 조회를 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id

			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			(1..100).toList().shuffled().forEach { i ->
				val reviewerId = memberRepository.insert(memberFixture(email = Email("aaaa$i@gmail.com"))).id
				quizReviewRepository.insert(quizReviewFixture(starRating = i, memberId = reviewerId, quizId = bookQuizId))
			}

			adapter
				.findAllQuizReviewSummaryBy(
					ReadQuizReviewSummaryCondition(bookQuizId),
					PageOption.of(),
				).size shouldBe 100
		}

		"요약본 조회를 정렬하여 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			(1..50).toList().shuffled().forEach { i ->
				val reviewerId1 = memberRepository.insert(memberFixture(email = Email("aaaa$i@gmail.com"))).id
				val reviewerId2 = memberRepository.insert(memberFixture(email = Email("bbbb$i@gmail.com"))).id

				quizReviewRepository.insert(
					quizReviewFixture(starRating = i, comment = null, memberId = reviewerId1, quizId = bookQuizId),
				)
				quizReviewRepository.insert(
					quizReviewFixture(starRating = i, comment = "hello", memberId = reviewerId2, quizId = bookQuizId),
				)
			}

			adapter
				.findAllQuizReviewSummaryBy(
					ReadQuizReviewSummaryCondition(bookQuizId),
					PageOption.of(sort = QuizReviewSummarySortKeyword.STAR_RATING, direction = SortDirection.DESC),
				).toList()[0]
				.starRating shouldBe 50

			adapter
				.findAllQuizReviewSummaryBy(
					ReadQuizReviewSummaryCondition(bookQuizId),
					PageOption.of(sort = QuizReviewSummarySortKeyword.UPDATED_AT),
				).toList()
				.shouldNotBeEmpty()
		}

		"내가 작성한 퀴즈 리뷰를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))

			adapter.findMyReviewBy(bookQuizId, memberId) shouldNotBe null
			adapter.findMyReviewBy(bookQuizId, memberId + 1) shouldBe null
			adapter.findMyReviewBy(bookQuizId + 1, memberId) shouldBe null
		}
	})