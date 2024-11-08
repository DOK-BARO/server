package kr.kro.dokbaro.server.core.solvingquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.BookSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolvingQuizSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizContributor
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizCreator
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture
import java.time.LocalDateTime
import java.util.UUID

class SolvingQuizQueryServiceTest :
	StringSpec({
		val findBookQuizUseCase = mockk<FindBookQuizUseCase>()
		val loadSolvingQuizPort = mockk<LoadSolvingQuizPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val readMySolveSummaryPort = mockk<ReadMySolveSummaryPort>()
		val readMyStudyGroupSolveSummaryPort = mockk<ReadMyStudyGroupSolveSummaryPort>()

		val solvingQuizQueryService =
			SolvingQuizQueryService(
				findBookQuizUseCase,
				loadSolvingQuizPort,
				findCertificatedMemberUseCase,
				readMySolveSummaryPort,
				readMyStudyGroupSolveSummaryPort,
			)

		"퀴즈 결과 탐색을 수행한다" {
			every { loadSolvingQuizPort.findById(any()) } returns
				SolvingQuiz(
					playerId = 1,
					quizId = 1,
					id = 1,
					sheets = mutableMapOf(1L to AnswerSheet(listOf("O")), 2L to AnswerSheet(listOf("X"))),
				)

			every { findBookQuizUseCase.findBy(any()) } returns
				bookQuizFixture(
					id = 1,
					questions =
						listOf(
							quizQuestionFixture(id = 1, answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O")))),
							quizQuestionFixture(id = 2, answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X")))),
						),
				)

			val result: TotalGradeResult = solvingQuizQueryService.findAllBy(1)

			result.solvingQuizId shouldBe 1
			result.quizId shouldBe 1
			result.playerId shouldBe 1
			result.questionCount shouldBe 2
			result.correctCount shouldBe 2
		}

		"퀴즈 결과 탐색 시 해당 풀이에 대한 ID를 탐색할 수 없으면 예외를 반환한다" {
			every { loadSolvingQuizPort.findById(any()) } returns null

			shouldThrow<NotFoundSolvingQuizException> {
				solvingQuizQueryService.findAllBy(5)
			}
		}

		"내가 푼 퀴즈 목록을 조회한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { readMySolveSummaryPort.findAllMySolveSummary(any()) } returns
				listOf(
					MySolveSummary(
						id = 1L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
						bookImageUrl = "https://example.com/book1.jpg",
						quiz =
							MySolvingQuizSummary(
								id = 101L,
								title = "Math Quiz",
							),
					),
					MySolveSummary(
						id = 2L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 15, 30),
						bookImageUrl = "https://example.com/book2.jpg",
						quiz =
							MySolvingQuizSummary(
								id = 102L,
								title = "Science Quiz",
							),
					),
				)

			solvingQuizQueryService.findAllMySolveSummary(UUID.randomUUID()) shouldNotBe null
		}

		"그룹 내 내가 푼 퀴즈 목록을 조회한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { readMyStudyGroupSolveSummaryPort.findAllMyStudyGroupSolveSummary(any(), any()) } returns
				listOf(
					StudyGroupSolveSummary(
						id = 1L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
						book =
							BookSummary(
								id = 1L,
								title = "Mathematics Essentials",
								imageUrl = "https://example.com/book1.jpg",
							),
						quiz =
							QuizSummary(
								id = 101L,
								title = "Algebra Quiz",
								creator =
									QuizCreator(
										id = 1L,
										nickname = "MathGuru",
										profileImageUrl = "https://example.com/profile1.jpg",
									),
								createdAt = LocalDateTime.of(2024, 10, 1, 8, 30),
								contributors =
									listOf(
										QuizContributor(
											id = 2L,
											nickname = "AlgebraAce",
											profileImageUrl = "https://example.com/profile2.jpg",
										),
										QuizContributor(
											id = 3L,
											nickname = "GeometryGeek",
											profileImageUrl = null,
										),
									),
							),
					),
					StudyGroupSolveSummary(
						id = 2L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 15, 30),
						book =
							BookSummary(
								id = 2L,
								title = "Science Basics",
								imageUrl = "https://example.com/book2.jpg",
							),
						quiz =
							QuizSummary(
								id = 102L,
								title = "Physics Quiz",
								creator =
									QuizCreator(
										id = 2L,
										nickname = "ScienceSage",
										profileImageUrl = "https://example.com/profile3.jpg",
									),
								createdAt = LocalDateTime.of(2024, 9, 25, 14, 45),
								contributors =
									listOf(
										QuizContributor(
											id = 4L,
											nickname = "PhysicsFan",
											profileImageUrl = "https://example.com/profile4.jpg",
										),
										QuizContributor(
											id = 5L,
											nickname = "ChemistryChamp",
											profileImageUrl = "https://example.com/profile5.jpg",
										),
									),
							),
					),
				)

			solvingQuizQueryService.findAllMyStudyGroupSolveSummary(UUID.randomUUID(), 1) shouldNotBe null
		}
	})