package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class CreateBookQuizCommand(
	val title: String,
	val description: String,
	val bookId: Long,
	val loginUserId: Long,
	val loginUserNickname: String,
	val questions: Collection<Question>,
	val studyGroupId: Long? = null,
	val timeLimitSecond: Int? = null,
	val viewScope: AccessScope,
	val editScope: AccessScope,
) {
	data class Question(
		val content: String,
		val selectOptions: Collection<String> = emptyList(),
		val answerExplanationContent: String,
		val answerExplanationImages: Collection<String> = emptyList(),
		val answerType: QuizType,
		val answers: Collection<String> = emptyList(),
	)
}