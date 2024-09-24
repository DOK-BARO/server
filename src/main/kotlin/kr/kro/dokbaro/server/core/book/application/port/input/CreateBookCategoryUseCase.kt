package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand

fun interface CreateBookCategoryUseCase {
	fun create(command: CreateBookCategoryCommand): Long
}