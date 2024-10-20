package kr.kro.dokbaro.server.core.quizreview.application.port.out

import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement

fun interface ReadQuizReviewTotalScorePort {
	fun findBy(quizId: Long): Collection<QuizReviewTotalScoreElement>
}