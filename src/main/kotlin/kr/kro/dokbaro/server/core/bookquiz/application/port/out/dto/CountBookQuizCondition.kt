package kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope

data class CountBookQuizCondition(
	val bookId: Long? = null,
	val creatorId: Long? = null,
	val studyGroupId: Long? = null,
	val solved: Solved? = null,
	val viewScope: AccessScope? = null,
) {
	data class Solved(
		val memberId: Long,
		val solved: Boolean,
	)
}