package kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope

data class CountBookQuizCondition(
	val bookId: Long? = null,
	val creatorId: Long? = null,
	val studyGroup: StudyGroup = StudyGroup(),
	val solved: Solved? = null,
	val temporary: Boolean = false,
	val viewScope: AccessScope? = null,
) {
	data class Solved(
		val memberId: Long,
		val solved: Boolean,
	)

	data class StudyGroup(
		val only: Boolean = false,
		val exclude: Boolean = false,
		val id: Long? = null,
	)
}