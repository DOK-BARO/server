package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand
import kr.kro.dokbaro.server.core.solvingquiz.query.SolveResult

fun interface SolveQuestionUseCase {
	fun solve(command: SolveQuestionCommand): SolveResult
}