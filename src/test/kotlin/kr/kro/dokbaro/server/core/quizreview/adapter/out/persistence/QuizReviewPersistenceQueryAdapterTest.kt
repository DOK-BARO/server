package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq.QuizReviewMapper
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewQueryRepository
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortOption
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
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(comment = "hello", memberId = memberId, quizId = bookQuizId))

			adapter.findBy(bookQuizId).size shouldBe 2
		}

		"개수를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			quizReviewRepository.insert(quizReviewFixture(memberId = memberId, quizId = bookQuizId))
			quizReviewRepository.insert(quizReviewFixture(comment = "hello", memberId = memberId, quizId = bookQuizId))

			adapter.countBy(CountQuizReviewCondition(bookQuizId)) shouldBe 2
		}
		val defaultPageOption = PageOption(0, 200_000_000)
		val defaultSortOption = SortOption(QuizReviewSummarySortOption.CREATED_AT)

		"요약본 조회를 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			(1..100).toList().shuffled().forEach { i ->
				quizReviewRepository.insert(quizReviewFixture(starRating = i, memberId = memberId, quizId = bookQuizId))
			}

			adapter
				.findAllQuizReviewSummaryBy(
					ReadQuizReviewSummaryCondition(bookQuizId),
					defaultPageOption,
					defaultSortOption,
				).size shouldBe 100
		}

		"요약본 조회를 정렬하여 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = memberId, bookId = bookId))

			(1..50).toList().shuffled().forEach { i ->
				quizReviewRepository.insert(
					quizReviewFixture(starRating = i, comment = null, memberId = memberId, quizId = bookQuizId),
				)
				quizReviewRepository.insert(
					quizReviewFixture(starRating = i, comment = "hello", memberId = memberId, quizId = bookQuizId),
				)
			}

			adapter
				.findAllQuizReviewSummaryBy(
					ReadQuizReviewSummaryCondition(bookQuizId),
					defaultPageOption,
					SortOption(QuizReviewSummarySortOption.STAR_RATING, SortDirection.DESC),
				).toList()[0]
				.starRating shouldBe 50
		}
	})