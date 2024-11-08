package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary

fun interface ReadMySolveSummaryPort {
	fun findAllMySolveSummary(memberId: Long): Collection<MySolveSummary>
}