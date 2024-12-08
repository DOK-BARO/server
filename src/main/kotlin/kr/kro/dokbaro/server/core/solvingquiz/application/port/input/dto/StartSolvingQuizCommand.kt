package kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto

data class StartSolvingQuizCommand(
	val memberId: Long,
	val quizId: Long,
)