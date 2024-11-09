package kr.kro.dokbaro.server.core.quizreview.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture

class QuizReviewTest :
	StringSpec({

		"리뷰를 수정한다" {
			val quizReview = quizReviewFixture()

			val newStarRating = 1
			val newDifficultyLevel = 3
			val newComment = "newHello"

			quizReview.changeReview(newStarRating, newDifficultyLevel, newComment)

			quizReview.starRating shouldBe newStarRating
			quizReview.difficultyLevel shouldBe newDifficultyLevel
			quizReview.comment shouldBe newComment
		}
	})