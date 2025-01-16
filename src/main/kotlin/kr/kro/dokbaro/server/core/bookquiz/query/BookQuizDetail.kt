package kr.kro.dokbaro.server.core.bookquiz.query

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class BookQuizDetail(
	val id: Long,
	val title: String,
	val description: String,
	val bookId: Long,
	val questions: Collection<Question>,
	val studyGroupId: Long?,
	val timeLimitSecond: Int?,
	val viewScope: AccessScope,
	val editScope: AccessScope,
) {
	data class Question(
		val id: Long,
		val content: String,
		val selectOptions: Collection<String> = emptyList(),
		val answerExplanationContent: String,
		val answerExplanationImages: Collection<String> = emptyList(),
		val answerType: QuizType,
		val answers: Collection<String> = emptyList(),
	)
}