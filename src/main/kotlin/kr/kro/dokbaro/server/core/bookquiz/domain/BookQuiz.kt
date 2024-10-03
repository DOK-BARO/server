package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class BookQuiz(
	val title: String,
	val description: String,
	val bookId: Long,
	val creatorId: Long,
	val questions: Collection<QuizQuestion>,
	val studyGroups: Collection<Long> = emptyList(),
	val id: Long = Constants.UNSAVED_ID,
)