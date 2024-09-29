package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class BookQuiz(
	val title: String,
	val description: String,
	val bookId: Long,
	val creatorId: Long,
	val id: Long = Constants.UNSAVED_ID,
)