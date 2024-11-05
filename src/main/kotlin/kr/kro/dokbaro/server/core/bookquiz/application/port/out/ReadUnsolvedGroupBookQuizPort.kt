package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary

fun interface ReadUnsolvedGroupBookQuizPort {
	fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
	): Collection<UnsolvedGroupBookQuizSummary>
}