package kr.kro.dokbaro.server.core.solvingquiz.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword

fun interface FindAllMyStudyGroupSolveSummaryUseCase {
	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>,
	): PageResponse<StudyGroupSolveSummary>
}