package kr.kro.dokbaro.server.core.quizquestionreport.application.service

import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.CreateQuizQuestionReportUseCase
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.dto.CreateQuizQuestionReportCommand
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.out.InsertQuizQuestionReportPort
import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport
import org.springframework.stereotype.Service

@Service
class QuizQuestionReportService(
	private val insertQuizQuestionReportPort: InsertQuizQuestionReportPort,
) : CreateQuizQuestionReportUseCase {
	override fun create(command: CreateQuizQuestionReportCommand): Long =
		insertQuizQuestionReportPort.insert(
			QuizQuestionReport(
				questionId = command.questionId,
				reporterId = command.reporterId,
				content = command.content,
			),
		)
}