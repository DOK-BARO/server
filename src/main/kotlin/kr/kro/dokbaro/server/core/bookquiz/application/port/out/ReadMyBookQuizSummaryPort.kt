package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary

fun interface ReadMyBookQuizSummaryPort {
	fun findAllMyBookQuiz(memberId: Long): Collection<MyBookQuizSummary>
}