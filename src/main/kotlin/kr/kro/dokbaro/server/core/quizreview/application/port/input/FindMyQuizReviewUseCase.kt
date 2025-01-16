package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.core.quizreview.query.MyQuizReview

fun interface FindMyQuizReviewUseCase {
	fun findMyReviewBy(
		quizId: Long,
		memberId: Long,
	): MyQuizReview?
}