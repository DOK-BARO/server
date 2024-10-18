package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import org.springframework.stereotype.Service

@Service
class QuizReviewService(
	private val insertQuizReviewPort: InsertQuizReviewPort,
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
) : CreateQuizReviewUseCase {
	override fun create(command: CreateQuizReviewCommand): Long {
		val memberId = findCertificatedMemberUseCase.getByCertificationId(command.authId).id
		return insertQuizReviewPort.insert(
			QuizReview(
				score = command.score,
				difficultyLevel = command.difficultyLevel,
				comment = command.comment,
				memberId = memberId,
				quizId = command.quizId,
			),
		)
	}
}