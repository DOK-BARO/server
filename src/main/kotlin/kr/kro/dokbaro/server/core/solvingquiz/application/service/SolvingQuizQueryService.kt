package kr.kro.dokbaro.server.core.solvingquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeResult
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import org.springframework.stereotype.Service

@Service
class SolvingQuizQueryService(
	private val findBookQuizUseCase: FindBookQuizUseCase,
	private val loadSolvingQuizPort: LoadSolvingQuizPort,
) : FindAllSolveResultUseCase {
	override fun findAllBy(solvingQuizId: Long): TotalGradeResult {
		val solvingQuiz: SolvingQuiz =
			loadSolvingQuizPort.findById(solvingQuizId) ?: throw NotFoundSolvingQuizException(solvingQuizId)

		val quiz: BookQuiz = findBookQuizUseCase.findBy(solvingQuiz.quizId)

		val gradeResultMap: Map<Long, GradeResult> = quiz.gradeAll(solvingQuiz.getSheets())

		return TotalGradeResult(
			solvingQuizId = solvingQuizId,
			quizId = quiz.id,
			playerId = solvingQuiz.playerId,
			questionCount = quiz.getQuestionCount(),
			correctCount = gradeResultMap.values.count { it.correct },
		)
	}
}