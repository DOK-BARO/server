package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import java.util.UUID

fun interface FindAllMySolveSummaryUseCase {
	fun findAllMySolveSummary(authId: UUID): Collection<MySolveSummary>
}