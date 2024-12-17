package kr.kro.dokbaro.server.core.solvingquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.CountSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture
import java.time.LocalDateTime

class SolvingQuizQueryServiceTest :
	StringSpec({
		val findBookQuizUseCase = mockk<FindBookQuizUseCase>()
		val loadSolvingQuizPort = mockk<LoadSolvingQuizPort>()
		val readMySolveSummaryPort = mockk<ReadMySolveSummaryPort>()
		val readMyStudyGroupSolveSummaryPort = mockk<ReadMyStudyGroupSolveSummaryPort>()
		val countSolvingQuizPort = mockk<CountSolvingQuizPort>()

		val solvingQuizQueryService =
			SolvingQuizQueryService(
				findBookQuizUseCase,
				loadSolvingQuizPort,
				readMySolveSummaryPort,
				readMyStudyGroupSolveSummaryPort,
				countSolvingQuizPort,
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
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
							quizQuestionFixture(
								id = 2,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
							),
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
			every { readMySolveSummaryPort.findAllMySolveSummary(any(), any()) } returns
				listOf(
					MySolveSummary(
						id = 1L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
						bookImageUrl = "https://example.com/book1.jpg",
						quiz =
							MySolveSummary.Quiz(
								id = 101L,
								title = "Math Quiz",
							),
					),
					MySolveSummary(
						id = 2L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 15, 30),
						bookImageUrl = "https://example.com/book2.jpg",
						quiz =
							MySolveSummary.Quiz(
								id = 102L,
								title = "Science Quiz",
							),
					),
				)

			every { countSolvingQuizPort.countBy(any()) } returns 2

			solvingQuizQueryService.findAllMySolveSummary(1, PageOption.of()) shouldNotBe null
		}

		"그룹 내 내가 푼 퀴즈 목록을 조회한다" {
			every { readMyStudyGroupSolveSummaryPort.findAllMyStudyGroupSolveSummary(any(), any()) } returns
				listOf(
					StudyGroupSolveSummary(
						id = 1L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
						book =
							StudyGroupSolveSummary.Book(
								id = 1L,
								title = "Mathematics Essentials",
								imageUrl = "https://example.com/book1.jpg",
							),
						quiz =
							StudyGroupSolveSummary.Quiz(
								id = 101L,
								title = "Algebra Quiz",
								creator =
									StudyGroupSolveSummary.Creator(
										id = 1L,
										nickname = "MathGuru",
										profileImageUrl = "https://example.com/profile1.jpg",
									),
								createdAt = LocalDateTime.of(2024, 10, 1, 8, 30),
								contributors =
									listOf(
										StudyGroupSolveSummary.Contributor(
											id = 2L,
											nickname = "AlgebraAce",
											profileImageUrl = "https://example.com/profile2.jpg",
										),
										StudyGroupSolveSummary.Contributor(
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
							StudyGroupSolveSummary.Book(
								id = 2L,
								title = "Science Basics",
								imageUrl = "https://example.com/book2.jpg",
							),
						quiz =
							StudyGroupSolveSummary.Quiz(
								id = 102L,
								title = "Physics Quiz",
								creator =
									StudyGroupSolveSummary.Creator(
										id = 2L,
										nickname = "ScienceSage",
										profileImageUrl = "https://example.com/profile3.jpg",
									),
								createdAt = LocalDateTime.of(2024, 9, 25, 14, 45),
								contributors =
									listOf(
										StudyGroupSolveSummary.Contributor(
											id = 4L,
											nickname = "PhysicsFan",
											profileImageUrl = "https://example.com/profile4.jpg",
										),
										StudyGroupSolveSummary.Contributor(
											id = 5L,
											nickname = "ChemistryChamp",
											profileImageUrl = "https://example.com/profile5.jpg",
										),
									),
							),
					),
				)

			solvingQuizQueryService.findAllMyStudyGroupSolveSummary(1, 1) shouldNotBe null
		}
	})