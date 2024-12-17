package kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto

data class CountBookQuizCondition(
	val bookId: Long? = null,
	val creatorId: Long? = null,
)