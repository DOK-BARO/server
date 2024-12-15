package kr.kro.dokbaro.server.core.solvingquiz.application.service

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizByQuestionIdUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.StartSolvingQuizCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.InsertSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.UpdateSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture

class SolvingQuizServiceTest :
	StringSpec({
		val insertSolvingQuizPort = mockk<InsertSolvingQuizPort>()
		val loadSolvingQuizPort = mockk<LoadSolvingQuizPort>()
		val updateSolvingQuizPort = mockk<UpdateSolvingQuizPort>()
		val findBookQuizByQuestionIdUseCase = mockk<FindBookQuizByQuestionIdUseCase>()
		val solvingQuizService =
			SolvingQuizService(
				insertSolvingQuizPort,
				loadSolvingQuizPort,
				updateSolvingQuizPort,
				findBookQuizByQuestionIdUseCase,
			)
		afterEach {
			clearAllMocks()
		}

		"퀴즈 풀기를 시작한다" {
			every { insertSolvingQuizPort.insert(any()) } returns 1

			solvingQuizService.start(
				StartSolvingQuizCommand(
					1,
					1,
				),
			) shouldBe 1
		}

		"퀴즈 문제를 풀 때 해당하는 풀이 ID가 없으면 예외를 반환한다" {
			every { loadSolvingQuizPort.findById(any()) } returns null

			val command =
				SolveQuestionCommand(
					1,
					0,
					listOf("3", "6"),
				)

			shouldThrow<NotFoundSolvingQuizException> {
				solvingQuizService.solve(command)
			}

			every { loadSolvingQuizPort.findById(any()) } returns SolvingQuiz(playerId = 1, quizId = 2)
			every { updateSolvingQuizPort.update(any()) } returns Unit
			every { findBookQuizByQuestionIdUseCase.findByQuestionId(any()) } returns bookQuizFixture()

			shouldNotThrow<NotFoundSolvingQuizException> {
				solvingQuizService.solve(command)
			}
		}
	})