package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword

fun interface ReadStudyGroupCollectionPort {
	fun findAllByStudyMemberId(
		memberId: Long,
		pageOption: PageOption<MyStudyGroupSortKeyword>,
	): Collection<StudyGroupSummary>
}