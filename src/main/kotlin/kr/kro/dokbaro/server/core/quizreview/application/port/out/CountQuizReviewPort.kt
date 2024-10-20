package kr.kro.dokbaro.server.core.quizreview.application.port.out

import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition

fun interface CountQuizReviewPort {
	fun countBy(condition: CountQuizReviewCondition): Long
}