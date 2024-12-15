package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface CreateBookQuizUseCase {
	fun create(
		command: CreateBookQuizCommand,
		user: DokbaroUser,
	): Long
}