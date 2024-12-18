package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword

fun interface FindUnsolvedGroupBookQuizUseCase {
	fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>,
	): PageResponse<UnsolvedGroupBookQuizSummary>
}