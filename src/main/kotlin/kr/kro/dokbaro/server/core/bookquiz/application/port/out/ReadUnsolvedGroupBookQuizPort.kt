package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword

fun interface ReadUnsolvedGroupBookQuizPort {
	fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>,
	): Collection<UnsolvedGroupBookQuizSummary>
}