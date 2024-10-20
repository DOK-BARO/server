package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview

fun quizReviewFixture(
	starRating: Int = 1,
	difficultyLevel: Int = 1,
	comment: String? = null,
	memberId: Long = Constants.UNSAVED_ID,
	quizId: Long = Constants.UNSAVED_ID,
	id: Long = Constants.UNSAVED_ID,
) = QuizReview(
	starRating = starRating,
	difficultyLevel = difficultyLevel,
	comment = comment,
	memberId = memberId,
	quizId = quizId,
	id = id,
)