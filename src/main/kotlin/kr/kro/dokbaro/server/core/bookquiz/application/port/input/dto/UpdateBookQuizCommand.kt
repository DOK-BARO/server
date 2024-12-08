package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class UpdateBookQuizCommand(
	val id: Long,
	val title: String,
	val description: String,
	val bookId: Long,
	val timeLimitSecond: Int?,
	val viewScope: AccessScope,
	val editScope: AccessScope,
	val studyGroupId: Long?,
	val questions: Collection<Question>,
	val modifierId: Long,
) {
	data class Question(
		val id: Long? = null,
		val content: String,
		val selectOptions: Collection<String> = emptyList(),
		val answerExplanationContent: String,
		val answerExplanationImages: Collection<String> = emptyList(),
		val answerType: QuizType,
		val answers: Collection<String> = emptyList(),
	)
}