package kr.kro.dokbaro.server.core.solvingquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture

class SolvingQuizQueryServiceTest :
	StringSpec({
		val findBookQuizUseCase = mockk<FindBookQuizUseCase>()
		val loadSolvingQuizPort = mockk<LoadSolvingQuizPort>()

		val solvingQuizQueryService = SolvingQuizQueryService(findBookQuizUseCase, loadSolvingQuizPort)

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
	})