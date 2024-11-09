package kr.kro.dokbaro.server.core.quizreview.application.port.input

fun interface DeleteQuizReviewUseCase {
	fun delete(id: Long)
}