package kr.kro.dokbaro.server.core.bookquiz.query.condition

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope

data class MyBookQuizSummaryFilterCondition(
	val temporary: Boolean = false,
	val viewScope: AccessScope? = null,
	val studyGroup: StudyGroup = StudyGroup(),
) {
	data class StudyGroup(
		val only: Boolean = false,
		val exclude: Boolean = false,
		val id: Long? = null,
	)
}