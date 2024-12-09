package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary

fun interface FindMyBookQuizUseCase {
	fun findMyBookQuiz(memberId: Long): Collection<MyBookQuizSummary>
}