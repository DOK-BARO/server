package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand

fun interface CreateQuizQuestionUseCase {
	fun create(command: CreateQuizQuestionCommand): Long
}