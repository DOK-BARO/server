package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword

fun interface FindMyBookQuizUseCase {
	fun findMyBookQuiz(
		memberId: Long,
		pageOption: PageOption<MyBookQuizSummarySortKeyword>,
	): PageResponse<MyBookQuizSummary>
}