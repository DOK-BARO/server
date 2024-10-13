package kr.kro.dokbaro.server.core.solvingquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.StartSolvingQuizCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.InsertSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.UpdateSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import org.springframework.stereotype.Service

@Service
class SolvingQuizService(
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
	private val insertSolvingQuizPort: InsertSolvingQuizPort,
	private val loadSolvingQuizPort: LoadSolvingQuizPort,
	private val updateSolvingQuizPort: UpdateSolvingQuizPort,
) : StartSolvingQuizUseCase,
	SolveQuestionUseCase {
	override fun start(command: StartSolvingQuizCommand): Long {
		val memberId = findCertificatedMemberUseCase.getByCertificationId(command.authId).id

		return insertSolvingQuizPort.insert(SolvingQuiz(memberId, command.quizId))
	}

	override fun solve(command: SolveQuestionCommand) {
		val solvingQuiz: SolvingQuiz =
			loadSolvingQuizPort.findById(command.solvingQuizId) ?: throw NotFoundSolvingQuizException(command.solvingQuizId)

		solvingQuiz.addSheet(command.questionId, AnswerSheet(command.answers))

		updateSolvingQuizPort.update(solvingQuiz)
	}
}