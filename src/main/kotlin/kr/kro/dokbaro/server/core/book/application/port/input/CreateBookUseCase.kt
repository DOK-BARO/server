package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand

fun interface CreateBookUseCase {
	fun create(command: CreateBookCommand): Long
}