package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition

fun interface CountSolvingQuizPort {
	fun countBy(condition: CountSolvingQuizCondition): Long
}