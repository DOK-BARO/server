package kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope

data class CountBookQuizCondition(
	val bookId: Long? = null,
	val creatorId: Long? = null,
	val studyGroup: StudyGroup = StudyGroup(),
	val solved: Solved? = null,
	val viewScope: AccessScope? = null,
) {
	data class StudyGroup(
		val active: Boolean = false,
		val id: Long? = null,
	) {
		companion object {
			fun of(id: Long?): StudyGroup = StudyGroup(active = true, id = id)
		}
	}

	data class Solved(
		val memberId: Long,
		val solved: Boolean,
	)
}