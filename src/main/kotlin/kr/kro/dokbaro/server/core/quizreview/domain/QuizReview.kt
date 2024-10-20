package kr.kro.dokbaro.server.core.quizreview.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizReview(
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String? = null,
	val memberId: Long,
	val quizId: Long,
	val id: Long = Constants.UNSAVED_ID,
)