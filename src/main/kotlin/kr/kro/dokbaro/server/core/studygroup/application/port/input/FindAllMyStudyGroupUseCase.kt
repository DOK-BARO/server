package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword

fun interface FindAllMyStudyGroupUseCase {
	fun findAll(
		memberId: Long,
		pageOption: PageOption<MyStudyGroupSortKeyword>,
	): PageResponse<StudyGroupSummary>
}