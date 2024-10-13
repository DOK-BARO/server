package kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto

data class SolveQuestionCommand(
	val solvingQuizId: Long,
	val questionId: Long,
	val answers: Collection<String>,
)