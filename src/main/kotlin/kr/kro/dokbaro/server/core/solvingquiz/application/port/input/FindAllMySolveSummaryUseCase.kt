package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary

fun interface FindAllMySolveSummaryUseCase {
	fun findAllMySolveSummary(memberId: Long): Collection<MySolveSummary>
}