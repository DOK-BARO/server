package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto

data class SolveQuestionRequest(
	val questionId: Long,
	val answers: Collection<String>,
)