package kr.kro.dokbaro.server.core.bookquiz.application.port.input

fun interface DeleteBookQuizUseCase {
	fun deleteBy(id: Long)
}