package kr.kro.dokbaro.server.core.bookquiz.query

import java.time.LocalDateTime

data class MyBookQuizSummary(
	val id: Long,
	val bookImageUrl: String?,
	val title: String,
	val description: String,
	val updatedAt: LocalDateTime,
	val studyGroup: StudyGroup? = null,
	val temporary: Boolean,
) {
	data class StudyGroup(
		val id: Long,
		val name: String,
		val profileImageUrl: String? = null,
	)
}