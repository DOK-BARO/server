package kr.kro.dokbaro.server.core.quizreview.application.port.out

import kr.kro.dokbaro.server.core.quizreview.query.MyQuizReview

fun interface ReadMyQuizReviewPort {
	fun findMyReviewBy(
		quizId: Long,
		memberId: Long,
	): MyQuizReview?
}