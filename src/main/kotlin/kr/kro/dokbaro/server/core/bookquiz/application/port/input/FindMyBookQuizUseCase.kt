package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import java.util.UUID

fun interface FindMyBookQuizUseCase {
	fun findMyBookQuiz(authId: UUID): Collection<MyBookQuizSummary>
}