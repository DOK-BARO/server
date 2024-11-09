package kr.kro.dokbaro.server.core.quizreviewreport.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.CreateQuizReviewReportUseCase
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.out.InsertQuizReviewReportPort
import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport
import org.springframework.stereotype.Service

@Service
class QuizReviewReportService(
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
	private val insertQuizReviewReportPort: InsertQuizReviewReportPort,
) : CreateQuizReviewReportUseCase {
	override fun create(command: CreateQuizReviewReportCommand): Long {
		val reporterId: Long = findCertificatedMemberUseCase.getByCertificationId(command.authId).id

		return insertQuizReviewReportPort.insert(
			QuizReviewReport(
				quizReviewId = command.quizReviewId,
				content = command.content,
				reporterId = reporterId,
			),
		)
	}
}