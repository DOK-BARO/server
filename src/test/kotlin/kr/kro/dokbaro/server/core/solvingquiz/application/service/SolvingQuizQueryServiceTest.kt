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
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.OXAnswer
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.CountSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadStudyGroupSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult
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
		val loadStudyGroupSolvingQuizPort = mockk<LoadStudyGroupSolvingQuizPort>()

		val solvingQuizQueryService =
			SolvingQuizQueryService(
				findBookQuizUseCase,
				loadSolvingQuizPort,
				readMySolveSummaryPort,
				readMyStudyGroupSolveSummaryPort,
				countSolvingQuizPort,
				loadStudyGroupSolvingQuizPort,
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

			val result: TotalGradeResult = solvingQuizQueryService.findAllGradeResultBy(1)

			result.solvingQuizId shouldBe 1
			result.quizId shouldBe 1
			result.playerId shouldBe 1
			result.questionCount shouldBe 2
			result.correctCount shouldBe 2
		}

		"퀴즈 결과 탐색 시 해당 풀이에 대한 ID를 탐색할 수 없으면 예외를 반환한다" {
			every { loadSolvingQuizPort.findById(any()) } returns null

			shouldThrow<NotFoundSolvingQuizException> {
				solvingQuizQueryService.findAllGradeResultBy(5)
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
			every { countSolvingQuizPort.countBy(any()) } returns 1000
			every { readMyStudyGroupSolveSummaryPort.findAllMyStudyGroupSolveSummary(any(), any(), any()) } returns
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

			solvingQuizQueryService.findAllMyStudyGroupSolveSummary(
				1,
				1,
				PageOption.of(),
			) shouldNotBe
				null
		}

		"스터디 그룹 맴버들의 퀴즈 점수를 조회한다" {
			val quizId = 1L
			every { findBookQuizUseCase.findBy(quizId) } returns
				bookQuizFixture(
					id = quizId,
					questions =
						listOf(
							quizQuestionFixture(id = 1, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							quizQuestionFixture(id = 2, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							quizQuestionFixture(id = 3, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
						),
				)
			every { loadStudyGroupSolvingQuizPort.findAllStudyGroupSolvingQuizSheets(any(), quizId) } returns
				mapOf(
					StudyGroupTotalGradeResult.Member(
						id = 1L,
						nickname = "코딩마스터",
						profileImageUrl = "https://example.com/images/profile1.jpg",
					) to
						SolvingQuiz(
							id = 1,
							playerId = 1,
							quizId = quizId,
							sheets =
								mutableMapOf(
									1L to AnswerSheet(listOf("X")),
									2L to AnswerSheet(listOf("X")),
									3L to AnswerSheet(listOf("X")),
								),
						),
					StudyGroupTotalGradeResult.Member(
						id = 2L,
						nickname = "알고리즘왕",
						profileImageUrl = null,
					) to
						SolvingQuiz(
							id = 2,
							playerId = 2,
							quizId = quizId,
							sheets =
								mutableMapOf(
									1L to AnswerSheet(listOf("X")),
									2L to AnswerSheet(listOf("X")),
									3L to AnswerSheet(listOf("O")),
								),
						),
					StudyGroupTotalGradeResult.Member(
						id = 3L,
						nickname = "알왕",
						profileImageUrl = null,
					) to
						SolvingQuiz(
							id = 3,
							playerId = 3,
							quizId = quizId,
							sheets =
								mutableMapOf(
									1L to AnswerSheet(listOf("O")),
									2L to AnswerSheet(listOf("O")),
									3L to AnswerSheet(listOf("X")),
								),
						),
					StudyGroupTotalGradeResult.Member(
						id = 4L,
						nickname = "왕",
						profileImageUrl = null,
					) to null,
				)

			val result = solvingQuizQueryService.findAllStudyGroupGradeResultBy(1, quizId)

			result.quizId shouldBe quizId
			result.totalQuestionCount shouldBe 3
			result.solvedMember.size shouldBe 3
			result.unSolvedMember.size shouldBe 1
			result.solvedMember.first().correctCount shouldBe 2
			result.solvedMember
				.first()
				.member.id shouldBe 3
			result.solvedMember.last().correctCount shouldBe 0
			result.solvedMember
				.last()
				.member.id shouldBe 1
		}

		"스터디 그룹 퀴즈 조회 시 푼 내역이 없으면 푼 사람에 카운팅하지 않는다" {
			val quizId = 1L
			every { findBookQuizUseCase.findBy(quizId) } returns
				bookQuizFixture(
					id = quizId,
					questions =
						listOf(
							quizQuestionFixture(id = 1, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							quizQuestionFixture(id = 2, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
							quizQuestionFixture(id = 3, answer = OXAnswer.from(AnswerSheet(listOf("O")))),
						),
				)
			every { loadStudyGroupSolvingQuizPort.findAllStudyGroupSolvingQuizSheets(any(), quizId) } returns
				mapOf(
					StudyGroupTotalGradeResult.Member(
						id = 1L,
						nickname = "코딩마스터",
						profileImageUrl = "https://example.com/images/profile1.jpg",
					) to
						SolvingQuiz(
							id = 1,
							playerId = 1,
							quizId = quizId,
							sheets =
								mutableMapOf(
									1L to AnswerSheet(listOf("X")),
									2L to AnswerSheet(listOf("X")),
									3L to AnswerSheet(listOf("X")),
								),
						),
					StudyGroupTotalGradeResult.Member(
						id = 2L,
						nickname = "알고리즘왕",
						profileImageUrl = null,
					) to
						SolvingQuiz(
							id = 2,
							playerId = 2,
							quizId = quizId,
							sheets =
								mutableMapOf(
									1L to AnswerSheet(listOf("X")),
									2L to AnswerSheet(listOf("X")),
									3L to AnswerSheet(listOf("O")),
								),
						),
					StudyGroupTotalGradeResult.Member(
						id = 3L,
						nickname = "알왕",
						profileImageUrl = null,
					) to
						SolvingQuiz(
							id = 3,
							playerId = 3,
							quizId = quizId,
						),
					StudyGroupTotalGradeResult.Member(
						id = 4L,
						nickname = "왕",
						profileImageUrl = null,
					) to null,
				)

			val result = solvingQuizQueryService.findAllStudyGroupGradeResultBy(1, quizId)

			result.quizId shouldBe quizId
			result.totalQuestionCount shouldBe 3
			result.solvedMember.size shouldBe 2
			result.unSolvedMember.size shouldBe 2
		}
	})