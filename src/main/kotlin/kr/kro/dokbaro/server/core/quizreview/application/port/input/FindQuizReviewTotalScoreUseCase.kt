package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore

fun interface FindQuizReviewTotalScoreUseCase {
	fun findTotalScoreBy(quizId: Long): QuizReviewTotalScore
}