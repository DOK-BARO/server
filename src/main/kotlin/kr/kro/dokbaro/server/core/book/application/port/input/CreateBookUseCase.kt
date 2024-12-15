package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface CreateBookUseCase {
	fun create(
		command: CreateBookCommand,
		user: DokbaroUser,
	): Long
}