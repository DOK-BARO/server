package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface DeleteBookQuizUseCase {
	fun deleteBy(
		id: Long,
		user: DokbaroUser,
	)
}