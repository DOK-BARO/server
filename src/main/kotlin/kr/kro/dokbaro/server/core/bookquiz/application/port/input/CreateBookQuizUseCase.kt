package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand

fun interface CreateBookQuizUseCase {
	fun create(command: CreateBookQuizCommand): Long
}