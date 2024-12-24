package kr.kro.dokbaro.server.core.solvingquiz.adapter.out

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
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.OXAnswer
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizQueryRepository
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRepository
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class SolvingQuizPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookRepository = BookRepository(dslContext)
		val bookQuizRepository = BookQuizRepository(dslContext, BookQuizMapper())
		val studyGroupRepository = StudyGroupRepository(dslContext)

		val solvingQuizRepository = SolvingQuizRepository(dslContext)
		val solvingQuizQueryRepository = SolvingQuizQueryRepository(dslContext, SolvingQuizMapper())

		val adapter = SolvingQuizPersistenceQueryAdapter(solvingQuizQueryRepository)

		"내가 푼 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val member2Id = memberRepository.insert(memberFixture(email = Email("sub@gmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())
			val firstQuizId = bookQuizRepository.insert(bookQuizFixture(title = "가", creatorId = memberId, bookId = bookId))
			val bookQuizId = bookQuizRepository.insert(bookQuizFixture(title = "나", creatorId = memberId, bookId = bookId))
			val lastQuizId = bookQuizRepository.insert(bookQuizFixture(title = "다", creatorId = memberId, bookId = bookId))

			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))
			val firstSolvingQuiz: Long =
				solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = bookQuizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = firstQuizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = lastQuizId))

			adapter.findAllMySolveSummary(memberId, PageOption.of()).size shouldBe 1
			adapter
				.findAllMySolveSummary(
					member2Id,
					PageOption.of(sort = MySolvingQuizSortKeyword.TITLE),
				).first()
				.quiz.id shouldBe
				firstQuizId
			adapter
				.findAllMySolveSummary(
					member2Id,
					PageOption.of(sort = MySolvingQuizSortKeyword.TITLE, direction = SortDirection.DESC),
				).first()
				.quiz.id shouldBe
				lastQuizId

			adapter
				.findAllMySolveSummary(
					member2Id,
					PageOption.of(sort = MySolvingQuizSortKeyword.CREATED_AT),
				).first()
				.id shouldBe
				firstSolvingQuiz
		}

		"그룹 내 내가 푼 퀴즈 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)
			val bookId = bookRepository.insertBook(bookFixture())
			val bookQuizId =
				bookQuizRepository.insert(
					bookQuizFixture(
						creatorId = memberId,
						bookId = bookId,
						studyGroupId = studyGroupId,
					),
				)

			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = bookQuizId))

			adapter.findAllMyStudyGroupSolveSummary(memberId, studyGroupId, PageOption.of()).shouldNotBeEmpty()
			adapter
				.findAllMyStudyGroupSolveSummary(
					memberId,
					studyGroupId,
					PageOption.of(sort = MyStudyGroupSolveSummarySortKeyword.UPDATED_AT),
				).shouldNotBeEmpty()
			adapter
				.findAllMyStudyGroupSolveSummary(
					memberId,
					studyGroupId,
					PageOption.of(sort = MyStudyGroupSolveSummarySortKeyword.TITLE),
				).shouldNotBeEmpty()
			adapter
				.findAllMyStudyGroupSolveSummary(memberId, studyGroupId, PageOption.of(direction = SortDirection.DESC))
				.shouldNotBeEmpty()
		}

		"푼 퀴즈의 개수를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val member2Id = memberRepository.insert(memberFixture(email = Email("sub@gmail.com"))).id
			val bookId = bookRepository.insertBook(bookFixture())

			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)
			val quizId = bookQuizRepository.insert(bookQuizFixture(title = "가", creatorId = memberId, bookId = bookId))
			val quizId2 =
				bookQuizRepository.insert(
					bookQuizFixture(title = "가", creatorId = memberId, bookId = bookId, studyGroupId = studyGroupId),
				)

			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = quizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = memberId, quizId = quizId2))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = quizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = quizId))
			solvingQuizRepository.insert(SolvingQuiz(playerId = member2Id, quizId = quizId))

			adapter.countBy(CountSolvingQuizCondition(memberId, studyGroupId = studyGroupId)) shouldBe 1
			adapter.countBy(CountSolvingQuizCondition(memberId)) shouldBe 2
			adapter.countBy(CountSolvingQuizCondition(member2Id)) shouldBe 3
			adapter.countBy(CountSolvingQuizCondition()) shouldBe 5
		}

		"스터디 그룹 내 퀴즈 시트를 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)
			val quizId =
				bookQuizRepository.insert(
					bookQuizFixture(
						title = "가",
						creatorId = memberId,
						bookId = bookId,
						questions =
							listOf(
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							),
					),
				)

			val questions = bookQuizRepository.load(quizId)!!.questions.getQuestions()

			solvingQuizRepository.insert(
				SolvingQuiz(
					playerId = memberId,
					quizId = quizId,
					sheets =
						mutableMapOf(
							questions[0].id to AnswerSheet(listOf("X")),
							questions[1].id to AnswerSheet(listOf("X")),
							questions[2].id to AnswerSheet(listOf("X")),
						),
				),
			)

			val result = adapter.findAllStudyGroupSolvingQuizSheets(studyGroupId, quizId)
			result.keys.size shouldBe 1
			result
				.values
				.first()!!
				.getSheets()
				.keys.size shouldBe 3
		}

		"id를 통한 푼 sovingQuiz 조회를 수행한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())

			val quizId =
				bookQuizRepository.insert(
					bookQuizFixture(
						title = "가",
						creatorId = memberId,
						bookId = bookId,
						questions =
							listOf(
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
								quizQuestionFixture(answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							),
					),
				)

			val questions = bookQuizRepository.load(quizId)!!.questions.getQuestions()

			val id =
				solvingQuizRepository.insert(
					SolvingQuiz(
						playerId = memberId,
						quizId = quizId,
						sheets =
							mutableMapOf(
								questions[0].id to AnswerSheet(listOf("X")),
								questions[1].id to AnswerSheet(listOf("X")),
								questions[2].id to AnswerSheet(listOf("X")),
							),
					),
				)

			adapter.findById(id) shouldNotBe null
		}
	})