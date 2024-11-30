package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz

fun interface LoadSolvingQuizPort {
	fun findById(solvingQuizId: Long): SolvingQuiz?
}