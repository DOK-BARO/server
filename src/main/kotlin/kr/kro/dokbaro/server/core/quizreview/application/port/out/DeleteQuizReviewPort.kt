package kr.kro.dokbaro.server.core.quizreview.application.port.out

fun interface DeleteQuizReviewPort {
	fun deleteBy(id: Long)
}