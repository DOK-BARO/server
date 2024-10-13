package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.StartSolvingQuizCommand

fun interface StartSolvingQuizUseCase {
	fun start(command: StartSolvingQuizCommand): Long
}