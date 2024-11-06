package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import java.util.UUID

fun interface FindUnsolvedGroupBookQuizUseCase {
	fun findAllUnsolvedQuizzes(
		memberAuthId: UUID,
		studyGroupId: Long,
	): Collection<UnsolvedGroupBookQuizSummary>
}