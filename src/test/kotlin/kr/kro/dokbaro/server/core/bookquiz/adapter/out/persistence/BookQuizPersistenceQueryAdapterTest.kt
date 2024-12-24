package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

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
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.repository.jooq.QuizReviewRepository
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRepository
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture
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
		val studyGroupRepository = StudyGroupRepository(dslContext)
		val quizReviewRepository = QuizReviewRepository(dslContext)
		val solvingQuizRepository = SolvingQuizRepository(dslContext)

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

		"OX 퀴즈 조회시에는 selectOption 이 명시되지 않는다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val bookQuizId: Long =
				bookQuizRepository.insert(
					bookQuizFixture(
						creatorId = member,
						bookId = book,
						questions =
							listOf(
								quizQuestionFixture(
									selectOptions = listOf(),
									answer =
										GradeSheetFactory.create(
											QuizType.OX,
											AnswerSheet(listOf("O")),
										),
								),
							),
					),
				)

			adapter
				.findBookQuizQuestionsBy(bookQuizId)!!
				.questions
				.toList()[0]
				.selectOptions.size shouldBe 0
		}

		"책에 대한 퀴즈의 개수를 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())
			val book2 = bookRepository.insertBook(bookFixture(isbn = "444"))
			val studyGroup =
				studyGroupRepository.insert(
					studyGroupFixture(studyMembers = mutableSetOf(StudyMember(member, StudyMemberRole.LEADER))),
				)

			val target = 10
			(1..target).forEach {
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book, title = "hello$it", studyGroupId = studyGroup),
				)
				bookQuizRepository.insert(
					bookQuizFixture(creatorId = member, bookId = book2, title = "hello$it"),
				)
			}

			adapter.countBookQuizBy(CountBookQuizCondition(bookId = book)) shouldBe target
			adapter.countBookQuizBy(CountBookQuizCondition(studyGroupId = studyGroup)) shouldBe target
			adapter.countBookQuizBy(CountBookQuizCondition(bookId = book2)) shouldBe target
			adapter.countBookQuizBy(CountBookQuizCondition(creatorId = member)) shouldBe target * 2
		}

		"내가 푼 퀴즈 개수를 조회한다" {
			val member = memberRepository.insert(memberFixture()).id
			val book = bookRepository.insertBook(bookFixture())

			val quiz =
				bookQuizRepository.insert(bookQuizFixture(creatorId = member, bookId = book))

			bookQuizRepository.insert(bookQuizFixture(creatorId = member, bookId = book))
			bookQuizRepository.insert(bookQuizFixture(creatorId = member, bookId = book))

			solvingQuizRepository.insert(SolvingQuiz(playerId = member, quizId = quiz))

			adapter.countBookQuizBy(
				CountBookQuizCondition(solved = CountBookQuizCondition.Solved(memberId = member, solved = true)),
			) shouldBe
				1
			adapter.countBookQuizBy(
				CountBookQuizCondition(solved = CountBookQuizCondition.Solved(memberId = member, solved = false)),
			) shouldBe
				2
		}

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

			adapter.findAllBookQuizSummary(book, PageOption.of()).size shouldBe targetSize
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
					PageOption.of(sort = BookQuizSummarySortKeyword.STAR_RATING),
				).toList()[0]
				.averageStarRating shouldBe 1.toDouble()

			adapter
				.findAllBookQuizSummary(
					book,
					PageOption.of(sort = BookQuizSummarySortKeyword.STAR_RATING, direction = SortDirection.DESC),
				).toList()[0]
				.averageStarRating shouldBe 10.toDouble()

			adapter
				.findAllBookQuizSummary(
					book,
					PageOption.of(sort = BookQuizSummarySortKeyword.CREATED_AT),
				).shouldNotBeEmpty()
			adapter
				.findAllBookQuizSummary(
					book,
					PageOption.of(sort = BookQuizSummarySortKeyword.UPDATED_AT),
				).shouldNotBeEmpty()
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
					pageOption = PageOption.of(),
				).shouldNotBeEmpty()
			adapter
				.findAllUnsolvedQuizzes(
					memberId = memberId,
					studyGroupId = studyGroupId,
					pageOption = PageOption.of(direction = SortDirection.DESC),
				).shouldNotBeEmpty()
			adapter
				.findAllUnsolvedQuizzes(
					memberId = memberId,
					studyGroupId = studyGroupId,
					pageOption = PageOption.of(sort = UnsolvedGroupBookQuizSortKeyword.TITLE),
				).shouldNotBeEmpty()
			adapter
				.findAllUnsolvedQuizzes(
					memberId = memberId,
					studyGroupId = studyGroupId,
					pageOption = PageOption.of(sort = UnsolvedGroupBookQuizSortKeyword.UPDATED_AT),
				).shouldNotBeEmpty()
		}

		"내가 제작한 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val memberId2 = memberRepository.insert(memberFixture(email = Email("hello@gmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())

			val quiz1 =
				bookQuizRepository.insert(
					bookQuizFixture(
						title = "A",
						bookId = bookId,
						creatorId = memberId,
					),
				)
			val quiz2 =
				bookQuizRepository.insert(
					bookQuizFixture(
						title = "B",
						bookId = bookId,
						creatorId = memberId,
					),
				)
			bookQuizRepository.insert(
				bookQuizFixture(
					title = "B",
					bookId = bookId,
					creatorId = memberId2,
				),
			)

			adapter
				.findAllMyBookQuiz(
					memberId,
					PageOption.of(),
				).size shouldBe
				2
			adapter
				.findAllMyBookQuiz(
					memberId2,
					PageOption.of(),
				).size shouldBe
				1
			adapter
				.findAllMyBookQuiz(
					memberId,
					PageOption.of(sort = MyBookQuizSummarySortKeyword.CREATED_AT),
				).first()
				.id shouldBe
				quiz1

			adapter
				.findAllMyBookQuiz(
					memberId,
					PageOption.of(sort = MyBookQuizSummarySortKeyword.UPDATED_AT),
				).first()
				.id shouldBe
				quiz1

			adapter
				.findAllMyBookQuiz(
					memberId,
					PageOption.of(sort = MyBookQuizSummarySortKeyword.TITLE),
				).first()
				.id shouldBe
				quiz1
			adapter
				.findAllMyBookQuiz(
					memberId,
					PageOption.of(sort = MyBookQuizSummarySortKeyword.TITLE, direction = SortDirection.DESC),
				).first()
				.id shouldBe
				quiz2
		}

		"퀴즈 설명을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuiz = bookQuizFixture(creatorId = memberId, bookId = bookId)
			val bookQuizId: Long = bookQuizRepository.insert(bookQuiz)

			val result = adapter.findExplanationBy(bookQuizId)

			result shouldNotBe null
			result!!.book.id shouldBe bookId
			result.creator.id shouldBe memberId
		}
	})