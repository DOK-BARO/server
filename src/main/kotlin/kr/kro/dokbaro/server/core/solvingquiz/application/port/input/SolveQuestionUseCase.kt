package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand

fun interface SolveQuestionUseCase {
	fun solve(command: SolveQuestionCommand)
}