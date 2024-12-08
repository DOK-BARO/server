package kr.kro.dokbaro.server.core.quizreviewreport.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.out.InsertQuizReviewReportPort
import java.util.UUID

class QuizReviewReportServiceTest :
	StringSpec({
		val insertQuizReviewReportPort = mockk<InsertQuizReviewReportPort>()

		val quizReviewReportService = QuizReviewReportService(insertQuizReviewReportPort)

		"quizReviewReport를 생성한다" {
			every { insertQuizReviewReportPort.insert(any()) } returns 1

			quizReviewReportService.create(
				CreateQuizReviewReportCommand(
					UUID.randomUUID(),
					1,
					"hello",
				),
			) shouldNotBe null
		}
	})