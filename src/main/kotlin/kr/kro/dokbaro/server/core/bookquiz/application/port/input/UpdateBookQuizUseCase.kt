package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand

fun interface UpdateBookQuizUseCase {
	fun update(command: UpdateBookQuizCommand)
}