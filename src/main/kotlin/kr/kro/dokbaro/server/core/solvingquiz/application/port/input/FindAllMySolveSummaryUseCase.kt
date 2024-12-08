package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary

fun interface FindAllMySolveSummaryUseCase {
	fun findAllMySolveSummary(loginUserId: Long): Collection<MySolveSummary>
}