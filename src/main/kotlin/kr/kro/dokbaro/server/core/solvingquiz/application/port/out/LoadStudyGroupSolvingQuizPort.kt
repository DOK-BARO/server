package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult

fun interface LoadStudyGroupSolvingQuizPort {
	fun findAllStudyGroupSolvingQuizSheets(
		studyGroupId: Long,
		quizId: Long,
	): Map<StudyGroupTotalGradeResult.Member, SolvingQuiz?>
}