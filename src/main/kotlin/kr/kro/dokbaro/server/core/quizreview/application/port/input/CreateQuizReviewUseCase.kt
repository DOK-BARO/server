package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand

fun interface CreateQuizReviewUseCase {
	fun create(command: CreateQuizReviewCommand): Long
}