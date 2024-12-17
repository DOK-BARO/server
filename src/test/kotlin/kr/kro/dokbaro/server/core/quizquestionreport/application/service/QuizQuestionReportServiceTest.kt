package kr.kro.dokbaro.server.core.quizquestionreport.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.dto.CreateQuizQuestionReportCommand
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.out.InsertQuizQuestionReportPort

class QuizQuestionReportServiceTest :
	StringSpec({

		val insertQuizQuestionReportPort: InsertQuizQuestionReportPort = mockk()

		val questionQuestionReportService = QuizQuestionReportService(insertQuizQuestionReportPort)

		"question 에 대해 신고한다" {
			every { insertQuizQuestionReportPort.insert(any()) } returns 1

			val command = CreateQuizQuestionReportCommand(1, 1, "hello")

			questionQuestionReportService.create(command) shouldNotBe null
		}
	})