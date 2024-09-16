package kr.kro.dokbaro.server.core.book.application.port.out.dto

data class ReadBookCollectionCondition(
	val title: String? = null,
	val authorName: String? = null,
	val description: String? = null,
	val categoryId: Long? = null,
)