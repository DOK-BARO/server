package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary

fun interface FindUnsolvedGroupBookQuizUseCase {
	fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
	): Collection<UnsolvedGroupBookQuizSummary>
}