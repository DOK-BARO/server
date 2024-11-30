package kr.kro.dokbaro.server.core.quizreview.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizReview(
	val id: Long = Constants.UNSAVED_ID,
	var starRating: Int,
	var difficultyLevel: Int,
	var comment: String? = null,
	val memberId: Long,
	val quizId: Long,
) {
	fun changeReview(
		starRating: Int,
		difficultyLevel: Int,
		comment: String? = null,
	) {
		this.starRating = starRating
		this.difficultyLevel = difficultyLevel
		this.comment = comment
	}
}