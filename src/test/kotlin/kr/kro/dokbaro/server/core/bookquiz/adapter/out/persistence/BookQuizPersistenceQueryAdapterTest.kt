package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq.BookQuizMapper
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class BookQuizPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({

		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val bookRepository = BookRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())
		val bookQuizQueryRepository = BookQuizQueryRepository(dslContext, BookQuizMapper())
		val studyGroupRepository = StudyGroupRepository(dslContext, StudyGroupMapper())
		val quizReviewRepository = QuizReviewRepository(dslContext)

		val adapter = BookQuizPersistenceQueryAdapter(bookQuizQueryRepository)

		"퀴즈 질문 목록을 조회한다" {

			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val bookQuiz = bookQuizFixture(creatorId = member, bookId = book)
			val bookQuizId: Long = bookQuizRepository.insert(bookQuiz)

			adapter.findBookQuizQuestionsBy(bookQuizId + 1) shouldBe null

			val target: BookQuizQuestions = adapter.findBookQuizQuestionsBy(bookQuizId)!!

			target.questions.size shouldBe bookQuiz.questions.getQuestions().size
			target.questions.sumOf { it.selectOptions.size } shouldBe
				bookQuiz.questions.getQuestions().sumOf { it.selectOptions.size }
		}

		"퀴즈 답변 목록을 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long = bookQuizRepository.insert(bookQuizFixture(creatorId = member, bookId = book))

			val bookQuiz: BookQuiz = bookQuizRepository.load(bookQuizId)!!

			adapter.findBookQuizAnswerBy(bookQuiz.questions.getQuestions()[0].id) shouldNotBe null
		}

		"책에 대한 퀴즈의 개수를 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val book2 = bookRepository.insertBook(bookFixture(isbn = "444"))

			val target = 10
			(1..target).forEach {
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book, title = "hello$it"),
				)
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book2, title = "hello$it"),
				)
			}

			adapter.countBookQuizBy(book) shouldBe target
			adapter.countBookQuizBy(book2) shouldBe target
		}

		val defaultPageOption = PageOption.of(1, 10000000)
		val defaultSortOption = SortOption(BookQuizSummarySortOption.CREATED_AT)
		"퀴즈 요약 목록을 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val book2 = bookRepository.insertBook(bookFixture(isbn = "444"))

			val targetSize = 10
			(1..targetSize).forEach {
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book, title = "hello$it"),
				)
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book2, title = "hello$it"),
				)
			}

			adapter.findAllBookQuizSummary(book, defaultPageOption, defaultSortOption).size shouldBe targetSize
		}

		"퀴즈 요약 목록을 정렬하여 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			(1..10).forEach {
				val quizId =
					bookQuizRepository.insert(
						bookQuizFixture(creatorId = member, bookId = book),
					)
				quizReviewRepository.insert(quizReviewFixture(memberId = member, quizId = quizId, starRating = it))
			}

			adapter
				.findAllBookQuizSummary(
					book,
					defaultPageOption,
					SortOption(BookQuizSummarySortOption.STAR_RATING),
				).toList()[0]
				.averageStarRating shouldBe 1.toDouble()

			adapter
				.findAllBookQuizSummary(
					book,
					defaultPageOption,
					SortOption(BookQuizSummarySortOption.STAR_RATING, SortDirection.DESC),
				).toList()[0]
				.averageStarRating shouldBe 10.toDouble()
		}

		"스터디 그룹 퀴즈 중 본인이 안 푼 문제 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val contributorId = memberRepository.insert(memberFixture(email = Email("cont@gmail.com"))).id

			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)

			val bookId = bookRepository.insertBook(bookFixture())

			bookQuizRepository.insert(
				bookQuizFixture(
					bookId = bookId,
					creatorId = memberId,
					studyGroupId = studyGroupId,
					contributorIds = mutableSetOf(contributorId),
				),
			)

			adapter
				.findAllUnsolvedQuizzes(
					memberId = memberId,
					studyGroupId = studyGroupId,
				).shouldNotBeEmpty()
		}

		"내가 제작한 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val memberId2 = memberRepository.insert(memberFixture(email = Email("hello@gmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())

			bookQuizRepository.insert(
				bookQuizFixture(
					bookId = bookId,
					creatorId = memberId,
				),
			)
			bookQuizRepository.insert(
				bookQuizFixture(
					bookId = bookId,
					creatorId = memberId2,
				),
			)

			adapter.findAllMyBookQuiz(memberId).size shouldBe 1
		}

		"퀴즈 설명을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuiz = bookQuizFixture(creatorId = memberId, bookId = bookId)
			val bookQuizId: Long = bookQuizRepository.insert(bookQuiz)

			val result = adapter.findExplanationBy(bookQuizId)

			result shouldNotBe null
			result!!.book.id shouldBe bookId
			result!!.creator.id shouldBe memberId
		}
	})