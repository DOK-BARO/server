package kr.kro.dokbaro.server.core.bookquiz.application.port.out

fun interface DeleteBookQuizPort {
	fun deleteBy(id: Long)
}