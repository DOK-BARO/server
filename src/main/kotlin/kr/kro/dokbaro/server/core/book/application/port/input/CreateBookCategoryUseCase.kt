package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface CreateBookCategoryUseCase {
	fun create(
		command: CreateBookCategoryCommand,
		user: DokbaroUser,
	): Long
}