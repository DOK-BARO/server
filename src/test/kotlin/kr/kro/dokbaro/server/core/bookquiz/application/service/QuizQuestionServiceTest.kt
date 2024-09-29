package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

class QuizQuestionServiceTest :
	StringSpec({
		val insertQuizQuestionPort = mockk<InsertQuizQuestionPort>()

		val quizQuestionService = QuizQuestionService(insertQuizQuestionPort)

		"퀴즈 질문을 생성한다" {
			every { insertQuizQuestionPort.insert(any()) } returns 1

			quizQuestionService.create(
				CreateQuizQuestionCommand(
					quizId = 1,
					content = "content",
					answerExplanation = "explanation",
					answerType = QuizType.FILL_BLANK,
					answers = listOf("answer"),
				),
			) shouldBe 1
		}
	})