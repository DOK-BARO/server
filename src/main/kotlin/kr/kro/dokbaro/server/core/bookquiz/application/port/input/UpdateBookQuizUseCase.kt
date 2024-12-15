package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface UpdateBookQuizUseCase {
	fun update(
		command: UpdateBookQuizCommand,
		user: DokbaroUser,
	)
}