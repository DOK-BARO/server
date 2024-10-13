package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz

fun interface InsertSolvingQuizPort {
	fun insert(solvingQuiz: SolvingQuiz): Long
}