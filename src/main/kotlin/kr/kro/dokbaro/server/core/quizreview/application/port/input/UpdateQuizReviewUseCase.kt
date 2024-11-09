package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.UpdateQuizReviewCommand

fun interface UpdateQuizReviewUseCase {
	fun update(command: UpdateQuizReviewCommand)
}