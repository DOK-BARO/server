package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult

fun interface FindAllSolveResultUseCase {
	fun findAllBy(solvingQuizId: Long): TotalGradeResult
}