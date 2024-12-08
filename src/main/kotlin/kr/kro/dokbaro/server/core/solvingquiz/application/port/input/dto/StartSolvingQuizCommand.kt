package kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto

data class StartSolvingQuizCommand(
	val loginUserId: Long,
	val quizId: Long,
)