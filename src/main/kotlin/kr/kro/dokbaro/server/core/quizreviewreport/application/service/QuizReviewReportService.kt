package kr.kro.dokbaro.server.core.quizreviewreport.application.service

import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.CreateQuizReviewReportUseCase
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.out.InsertQuizReviewReportPort
import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport
import org.springframework.stereotype.Service

@Service
class QuizReviewReportService(
	private val insertQuizReviewReportPort: InsertQuizReviewReportPort,
) : CreateQuizReviewReportUseCase {
	override fun create(command: CreateQuizReviewReportCommand): Long =
		insertQuizReviewReportPort.insert(
			QuizReviewReport(
				quizReviewId = command.quizReviewId,
				content = command.content,
				reporterId = command.loginUserId,
			),
		)
}