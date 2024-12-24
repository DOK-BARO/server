package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult

fun interface FindAllStudyGroupSolveResultUseCase {
	fun findAllStudyGroupGradeResultBy(
		studyGroupId: Long,
		quizId: Long,
	): StudyGroupTotalGradeResult
}