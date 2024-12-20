package kr.kro.dokbaro.server.core.solvingquiz.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword

fun interface ReadMyStudyGroupSolveSummaryPort {
	fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>,
	): Collection<StudyGroupSolveSummary>
}